package com.zqj.jdk.future;

/**
 * @author zqj
 * @create 2019-11-28 11:09
 */


import sun.misc.Unsafe;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * 执行的所有流程
 * NEW -> COMPLETING -> NORMAL
 * NEW -> COMPLETING -> EXCEPTIONAL
 * NEW -> CANCELLED
 * NEW -> INTERRUPTING -> INTERRUPTED
 */
public class MyFutureTask<V> implements Runnable {

    //任务的执行状态
    private volatile int state;
    //任务刚创建
    private static final int NEW = 0;
    //任务执行中
    private static final int COMPLETING = 1;
    //任务正常完成,大于此值均为不正常
    private static final int NORMAL = 2;
    //任务异常
    private static final int EXCEPTIONAL = 3;
    //任务手动取消
    private static final int CANCLED = 4;
    //任务中断中
    private static final int INTERRUPTING = 5;
    //任务已中断
    private static final int INTERUPTED = 6;

    //业务逻辑写在MyCallable的call里
    private MyCallable<V> callable;

    //返回的结果或者抛出的异常,V或者Exception的子类
    private Object outcome;

    //运行Mycallable的call()线程
    private volatile Thread runner;

    //等待的节点列表(链表)
    private volatile WaiterNode waiters;

    private static final Unsafe UNSAFE;

    private static final long stateOffset;

    private static final long runnerOffset;

    private static final long waitersOffset;


    static {
        try {
            //底层UNSAFE
            UNSAFE = Unsafe.getUnsafe();
            Class<?> clazz = MyFutureTask.class;
            //获取变量的内存地址,以使用cas操作,保持原子性操作
            stateOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("waiters"));

        } catch (Exception e) {
            throw new Error(e);
        }


    }

    public MyFutureTask(MyCallable callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        this.callable = callable;
        this.state = NEW;
    }

    //放弃任务，（isInterrupt为true，表示中断方式放弃，为false表示直接放弃）
    public boolean cancel(boolean isInterrupt) {
        //状态为NEW,然后cas将state替换为INTERRUPTING或CANCELD失败时直接返回cancel失败
        if (!(state == NEW && UNSAFE.compareAndSwapInt(this, stateOffset, NEW, isInterrupt ? INTERRUPTING : CANCLED))) {
            return false;
        }
        try {
            if (isInterrupt) {
                //如果是中断方式放弃
                try {
                    //获取当前执行中的线程
                    Thread t = runner;
                    if (t != null) {
                        //中断线程
                        t.interrupt();
                    }
                } finally {
                    //将状态强制替换为已中断
                    UNSAFE.putOrderedInt(this, stateOffset, INTERUPTED);
                }
            }
        } finally {
            //中断后唤醒等待的线程
            finishCompletion();
        }
        return true;
    }

    @Override
    public void run() {
        if (state != NEW || !UNSAFE.compareAndSwapObject(this, runnerOffset, null, Thread.currentThread())) {
            //如果此时状态不是NEW,或者改变当前持有线程的时候失败，则直接返回。这样保证只有一个线程在执行此方法（因为会进行2个非原子性操作，1个new对象时改变state为NEW，一个run时改变持有的线程。）
            return;
        }
        try {
            //用另一个指针指向callable对象
            MyCallable<V> callable = this.callable;

            if (callable != null && state == NEW) {
                //运行结果
                boolean ran;
                V result;
                try {
                    result = callable.call();
                    ran = true;

                } catch (Throwable e) {
                    result = null;
                    ran = false;
                    setException(e);
                }
                if (ran) {
                    set(result);
                }
            }

        } finally {
            //线程执行完则取消当前线程的绑定
            runner = null;
            int s = state;
            //如果状态是中断中,让出线程执行权
            if (s == INTERRUPTING) {
                handPossibleInterrupt(s);
            }
        }
    }

    private void handPossibleInterrupt(int s) {
        if (s == INTERRUPTING) {
            while (s == INTERRUPTING) {
                //等待cancel方法执行完成，将INTERRUPTING改为INTERRUPTED，暂时放出本线程的执行权，让其他线程优先执行
                Thread.yield();
            }
        }
    }

    //是否放弃
    public boolean isCancel() {
        return state >= CANCLED;
    }

    //是否完成
    public boolean isComplete() {
        return state != NEW;
    }

    //获取结果
    public V get() throws Exception {
        int s = state;
        //任务在进行中，则睡眠线程,等待被唤醒
        if (state <= COMPLETING) {
            //不超时返回，
            s = awaitDone(false, 0L);
        }
        return report(s);
    }

    //获取结果，超时，任然没数据，则抛出超时异常
    public V get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException, ExecutionException {
        if (unit == null) {
            throw new NullPointerException();
        }
        int s = state;
        //如果awaitDone()执行了后，超时返回的结果还是小于等于COMPLETING,则抛出超时异常。
        if (s <= COMPLETING && (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING) {
            throw new TimeoutException();
        }
        return report(s);
    }

    private V report(int s) throws ExecutionException {
        Object object = outcome;
        //正常状态直接返回
        if (s == NORMAL) {
            return (V) object;
        }
        //放弃之后的都为手动取消
        if (s >= CANCLED) {
            throw new CancellationException();
        }
        //剩下一个异常EXCEPTIONAL状态
        throw new ExecutionException((Throwable) object);
    }

    private int awaitDone(boolean timed, long nanos) throws InterruptedException {
        //超时的时间，如果设置超时返回,timed为true,则计算当前时间加上超时的时候之后的时间点。否则为0;
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        //定义一个node
        WaiterNode node = null;
        boolean queued = false;
        //自旋
        for (; ; ) {
            //首先响应中断
            //此方法会判断此线程是否被中断，并且会清除中断状态
            if (Thread.interrupted()) {
                //被中断移除此节点，node为空不会被处理
                removeWaiter(node);
                throw new InterruptedException();
            }
            int s = state;
            //此时状态大于COMPLETING则表示已经处理完成,可以直接返回状态
            if (s > COMPLETING) {
                return s;
            } else if (s == COMPLETING) {
                //此时刚好是COMPLETING,则表示任务可能即将被完成，设置线程的优先级为低，释放执行权，让其他线程先执行
                Thread.yield();
            }
            //剩下的就是状态是NEW的任务了
            //初次进来node是为空的
            else if (node == null) {
                node = new WaiterNode();
            } else if (!queued) {
                //初次!queued必为true，此时通过cas将waters改为自己定义的新的WaiterNode node(也就是将新的等待队列置于waiters链表的头部),同时将node的下一个节点指向之前的老的waiters，如果更新失败，下次进来继续更新
                //更新前 oldHeadWaiterNode -->WaiterNode -->oldTrail
                //更新后  newHeadWaiterNode -->oldHeadWaiterNode -->WaiterNode -->oldTrail
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset, node.next = waiters, node);
            } else if (timed) {
                //设置了超时返回
                //用设置的时间期限减去当前时间，如果小于等于0则表示已经超时，应该返回值了，否则睡眠线程
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
                    //在等待waiters中移除当前节点
                    removeWaiter(node);
                    return state;
                } else {
                    //睡眠线程
                    LockSupport.park(this);
                }
            } else {
                //未设置超时返回,直接睡眠线程
                LockSupport.park(this);
            }

        }
    }

    //从waiters中移除制定的节点
    private void removeWaiter(WaiterNode node) {
        if (node != null) {
            //将需要移除的node的thread置空，以区分
            node.thread = null;
            //轮询
            retry:
            for (; ; ) {
                for (WaiterNode pred = null, q = waiters, s; q != null; q = s) {
                    //node --> node --> node -->node
                    //pred --> q --> s
                    //q初始化为当前的waiters，每次pred暂存q的值，然后q指向下一个node(q.next);
                    s = q.next;
                    if (q.thread != null) {
                        //初次进来，是waiter的链表头部，如果不为空
                        //将q此时的值赋值到pred上面
                        pred = q;
                    } else if (pred != null) {
                        //进来此分支一般是第一个if判断到了需要取消的节点(q.thread ==null)，然后此步骤将需要取消的节点，的上一个节点，不再指向本身，而指向下一个
                        pred.next = s;
                        if (pred.thread == null) {
                            //如果此时pred的线程被置空了，说明此节点也被纳入了取消，则重新遍历
                            continue retry;
                        }
                    } else if (!UNSAFE.compareAndSwapObject(this, waitersOffset, q, s)) {
                        //进入此分支的条件是，第一次循环时，q.thread==null便满足，也就是waiter的链表表头就是要取消的节点，则cas更新waters由q变为他的下一个s
                        //更新失败重试
                        continue retry;
                    }
                }
                //正常遍历完，则停止轮询
                break;
            }

        }
    }

    //执行成功设置值
    private void set(V result) {
        //this替换的对应的对象，stateOffset内存地址值,NEW原先值,COMPLETING期待替换后的值
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            //改变成功则设置结果
            outcome = result;
            //强制改变状态为正常完成
            UNSAFE.putOrderedInt(this, stateOffset, NORMAL);
            //唤醒
            finishCompletion();
        }

    }

    //callable执行失败设置异常
    private void setException(Throwable e) {
        //尝试改变state为completing
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            //改变成功后，设置结果
            outcome = e;
            //设置后，强制改变state为异常的状态
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL);
            //改变后唤醒通知get()时所有休眠的线程，通知他们已经有结果了
            finishCompletion();
        }
    }

    //完成之后唤醒那些休眠的线程去获取结果了
    private void finishCompletion() {
        //遍历waiters这个链状结构的等待队列,依次唤醒，并清除掉。
        //w指向waters对象，然后将waters置为空，如果失败，则继续循环，直到成功改变为空，停止循环。
        for (WaiterNode w; (w = waiters) != null; ) {
            //使用cas将waters队列引用置空。
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, w, null)) {
                for (; ; ) {
                    //用t指向thread对象
                    Thread t = w.thread;
                    if (t != null) {
                        //help gc
                        w.thread = null;
                        //唤醒此线程
                        LockSupport.unpark(t);
                    }
                    //获取此节点的下一个等待中的节点
                    WaiterNode n = w.next;
                    if (n == null) {
                        //不存在下一个则停止循环
                        break;
                    }
                    //对于成员变量
                    //在两种情形下，可能会将某变量显式设为 null 。
                    //
                    //它是某个长生存期对象的成员，并且再也不会被该对象使用，而且比较大，在这种时候，将其设为 null 是一种优化。
                    //它是某个长生存期对象的成员，并且再也不会被该对象使用，并且已经被 dispose 了以释放其占用的资源。这里将其设为 null 是一种安全的做法，因为确定某人误用了一个 null 对象比确定某人误用了一个已被 dispose 的对象更容易些。
                    //help gc,断开w和next的连接，然后让w重新指向next,可以加速w之前指向的对象的垃圾回收速度。
                    w.next = null;
                    w = n;
                }
                //全部清理完成后停止最外面的循环
                break;
            }
        }
        //钩子方法，留给子类实现
        done();
        //一个线程执行完成后，清空callable
        callable = null;

    }

    //如果想在完成后执行一些自己的操作，只需重写此方法.
    protected void done() {
    }

    //等待链表队列节点
    static final class WaiterNode {
        //等待的线程
        private volatile Thread thread;
        //下一个节点
        private volatile WaiterNode next;


        WaiterNode() {
            this.thread = Thread.currentThread();
        }
    }
}
