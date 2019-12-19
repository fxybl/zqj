package com.zqj.jdk.aqs;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zqj
 * @create 2019-12-04 11:02
 */
public abstract class MyAbstractQueuedSynchronizer implements Serializable {

    private static final long serialVersionUID = 7373984972572414692L;

    //当前占有独占锁的线程
    private Thread exclusiveOwnerThread;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }

    public void setExclusiveOwnerThread(Thread exclusiveOwnerThread) {
        this.exclusiveOwnerThread = exclusiveOwnerThread;
    }

    protected MyAbstractQueuedSynchronizer() {

    }

    private static final Unsafe unsafe;
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waiterStatusOffset;
    private static final long nextOffset;

    static {
        try {
            //此处应改为使用反射获取，因为自写的类的类加载器不满足条件
            unsafe = Unsafe.getUnsafe();
            stateOffset = unsafe.objectFieldOffset(MyAbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(MyAbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(MyAbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waiterStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }



    /*
     *链表结构（头准确是不属于链表的等待队列里）
     *   * <pre>
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+
     * </pre>
     *
     * */

    //链状的节点
    static final class Node {

        //共享节点
        static final Node SHARED = new Node();

        //独占节点
        private final static Node EXCLUSIVE = null;

        //当前节点为放弃
        private static final int CANCELED = 1;

        //当前节点需要被唤醒
        private static final int SIGNAL = -1;

        //当前节点在condition排队中
        private static final int CONDITION = -2;

        //跟Condition有关
        private static final int PROPAGATE = -3;

        //当前节点的等待状态
        private volatile int waitStatus;

        //前一个节点
        private volatile Node prev;

        //后一个节点
        private volatile Node next;

        //当前节点的线程
        private Thread thread;

        //condition中使用的
        private Node nextWaiter;

        //新建Node的时候使用
        Node() {

        }

        //
        Node(Thread thread, Node mode) {
            this.thread = thread;
            //condition时mode不为空
            this.nextWaiter = mode;
        }

        Node(Thread thread, int waitStatus) {
            this.thread = thread;
            this.waitStatus = waitStatus;
        }

        public boolean isShared() {
            return this.nextWaiter == SHARED;
        }
    }

    //头结点（链表不包含头结点，头结点相当于当前持有锁的节点）
    private volatile Node head;


    //尾节点
    private volatile Node tail;

    //根据需求不同，意义不同，一般为0时，没有线程持有锁，大于0有线程持有锁
    private volatile int state;

    protected final int getState() {
        return state;
    }

    protected final void setState(int state) {
        this.state = state;
    }


    //cas,期望值，更新值
    protected final boolean compareAndSwapState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    //cas替换头结点
    protected final boolean compareAndSwapHead(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, expect, update);
    }

    //cas替换尾
    protected final boolean compareAndSwapTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    //cas替换node的waitStatus
    protected final boolean compareAndSwapWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waiterStatusOffset, expect, update);
    }

    //cas替换node的下一个等待结点
    protected final boolean compareAndSwapNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }

    //尝试获取锁,子类去实现
    protected boolean tryAcquire(int args) {
        throw new UnsupportedOperationException("你必须实现此方法--tryAcquire");
    }

    protected boolean tryRelease(int args) {
        throw new UnsupportedOperationException("你必须实现此方法--tryRelease");
    }

    //获取锁,lock()方法会调用此方法
    public final void acquire(int args) {
        //首先尝试获取锁，获取失败后，将当前节点以独占模式添加到等待队列中，然后再次尝试获取锁，获取失败睡眠线程，等待唤醒（acquireQueued返回true时是当前线程被中断）
        if (!tryAcquire(args) && acquireQueued(addWaiter(Node.EXCLUSIVE), args)) {
            //中断线程
            selfInterrupt();
        }
    }

    //释放锁，unlock()方法会调用此方法
    public final boolean release(int args) {
        if (tryRelease(args)) {
            Node h = head;
            //head waitStatus为0说明没有后继节点在等待,不需要唤醒
            if (h != null && h.waitStatus != 0) {
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }


    //从队列中获取锁
    private boolean acquireQueued(Node node, int args) {
        //添加到等待队列后，先不急着睡眠自己，先再次尝试获取一次锁，万一就获取成功了
        //默认失败
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (; ; ) {
                //获取此节点的上一个节点
                Node pred = node.prev;
                //如果此节点的上一个节点是头节点，便再一次获取锁，如果获取成功，就返回了
                if (pred == head && tryAcquire(args)) {
                    setHead(node);
                    //help gc
                    pred.next = null;
                    failed = false;
                    return interrupted;
                } else {
                    //上一个节点不是头节点或者获取锁失败
                    //判断是否应该睡眠线程，如果false,则继续轮询，如果true，则休眠线程，等被唤醒后便马上检查中断状态。如果返回true,那么interrupted为true,
                    //如果返回false,则不会执行下面的代码，而interrupted默认是false.
                    if (shouldParkAfterFailedAcquired(pred, node) && parkAndCheckInterrupt()) {
                        interrupted = true;
                    }

                }

            }
        } finally {
            //只有抛了异常，failed才会为true
            if (failed) {
                //放弃获取锁
                cancelAcquire(node);
            }

        }
    }

    //休眠线程，醒来后判断中断状态
    private boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        //此方法为静态方法，判断是否中断的同时，并清除中断状态
        //另一个动态的判断中断isInterrupted只判断中断状态，但是不会清除中断标记
        return Thread.interrupted();
    }

    //获取锁失败后，是否应该睡眠线程
    private boolean shouldParkAfterFailedAcquired(Node pred, Node node) {
        //获取上一个节点的状态
        int status = pred.waitStatus;
        //如果前面的节点设置为了SIGNAL状态，那么等他结束后会唤醒下一个节点,那么此节点就可以安心的睡眠了
        if (status == Node.SIGNAL) {
            return true;
        }
        //>0只有放弃状态，CANCELED,从pred开始，像前遍历，直到找到一个不是>0的节点，然后将此节点指向node
        if (status > 0) {
            do {
                //pred = pred.prev，将pred赋值为他的上一个节点，因为老的pred放弃了
                //node.prev = pred，
                //直到找到一个不是  CANCELED状态的节点
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);

            //找到了,此时返回false,说明不要进行休眠，继续上一个方法acquireQueued（）的轮询
            pred.next = node;
        } else {
            //状态为0,即初始状态，或者为PROPAGATE(为啥不为CONDITION?待看了Condition再来分析)
            //将状态设置为SIGNAL,不管成功与否，都返回false,重新走一遍acquireQueued（）的轮询...如果设置成功，则会进入本方法第一个if,返回true。
            compareAndSwapWaitStatus(pred, status, Node.SIGNAL);
        }
        return false;
    }

    //放弃获取锁
    private void cancelAcquire(Node node) {
        if (node == null) {
            return;
        }
        //先将node线程占有设置为空，也方便之后的判断，为空就是取消的节点
        node.thread = null;
        //轮询获取node的上一个不为放弃的节点（至于一个要放弃的节点node，它的prev为啥还要指向pred???）
        Node pred = node.prev;
        while (pred.waitStatus > 0) {
            node.prev = pred = pred.prev;
        }

        //node的上一个不为放弃的节点，它的next
        Node predNext = pred.next;

        //将node的waitStatus改为CANCELED,以便其他的线程进来时第一时间知道这个节点放弃了
        node.waitStatus = Node.CANCELED;

        //如果node为尾节点，则直接将pred设置为新的尾节点，同时将pred的下一个节点设置为空
        if (node == tail && compareAndSwapTail(node, pred)) {
            compareAndSwapNext(pred, node, null);
        } else {
            //如果不是尾节点，也不是头节点，并且状态是SIGNAL(不是SIGNAL,但是要<=0并且设置为SIGNAL要成功),同时pred的持有thread不能为空,
            int ws;
            if (pred != head && ((ws = pred.waitStatus) == Node.SIGNAL || (ws <= 0 && compareAndSwapWaitStatus(pred, ws, Node.SIGNAL)))) {
                //获取node.next，要求不为空同时不能为放弃，则将pred.next指向node.next
                Node next = node.next;
                if (next != null && next.waitStatus <= 0) {
                    //至于什么时候将next.prev指向pred,是靠其他线程来完成。
                    compareAndSwapNext(pred, predNext, next);
                }

            } else {
                //如果是头节点的话,唤醒node的下一个节点。
                unparkSuccessor(node);
            }
            //help gc
            node.next = node;
        }
    }

    //唤醒下一个节点, 从调用者看出，此node就是head节点
    private void unparkSuccessor(Node node) {
        int ws = node.waitStatus;
        //如果小于0，则设置为0
        if (ws < 0) {
            compareAndSwapWaitStatus(node, ws, 0);
        }
        //获取node的下一个节点
        Node s = node.next;
        //如果下一个节点为空或者为放弃状态,则从尾节点遍历，一直遍历到离node最近的一个有效节点进行唤醒，期间清除掉被取消的节点
        if (s == null || s.waitStatus > 0) {
            //如果为CANCELED状态也将s设置为空
            s = null;
            //初始化t = tail尾节点,终止循环的条件：t是null或者t是node本尊。每次将t赋值为t.prev上一个节点
            for (Node t = tail; t != null && t != node; t = t.prev) {
                //每次都将满足条件的t赋值给s
                if (t.waitStatus <= 0) {
                    s = t;
                }
            }
            //找到最近的一个s，如果一个条件都不满足，则不唤醒任何线程
            if (s != null) {
                LockSupport.unpark(s.thread);
            }
        }


    }

    //添加到等待队列中
    private Node addWaiter(Node mode) {
        //新建一个节点,
        Node node = new Node(Thread.currentThread(), mode);
        //获取尾节点
        Node pred = tail;
        //尾节点不为空
        if (pred != null) {
            //将node的上一个节点指向此时的尾节点
            node.prev = pred;
            //替换尾节点为当前节点
            if (compareAndSwapTail(pred, node)) {
                //如果直接替换成功了，则将之前的尾节点的下一个节点指向node
                pred.next = node;
                return node;
            }
        }
        //入队（执行到这里有2种可能，一种是tail就是空的，也就是此时是第一次添加节点到队列。第二种可能就是上面的cas替换尾节点为node的时候失败了，被其他线程给抢了）
        return enq(node);
    }

    //入队
    private Node enq(Node node) {
        //自旋入队
        for (; ; ) {
            Node pred = tail;
            if (pred == null) {
                //队列没有初始化时，tail和head都是空
                //cas设置头
                if (compareAndSwapHead(null, new Node())) {
                    tail = head;
                }
            } else {
                //入队
                node.prev = pred;
                if (compareAndSwapTail(pred, node)) {
                    pred.next = node;
                    return node;
                }
            }
        }

    }

    private void setHead(Node node) {
        //获取了锁后，相当于此线程就已经拥有了头节点的所有权，所以不用cas进行设置
        this.head = node;
        //头节点没有持有节点
        node.thread = null;
        //头节点不存在上一个节点
        node.prev = null;
    }

    //中断自己
    private void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    //当前节点的前面是否有节点在排队
    protected final boolean hasQueuePred() {
        Node h = head;
        Node t = tail;
        Node s;
        //1：头等于尾返回false，因为只有未初始化时头才会等会尾，都为空,此时没有队列
        //2：如果头不等于尾，那么肯定有队列,获取头的下一个节点,如果为空，直接返回true（此时另一个节点正在进行初始化操作,方法enq(Node node)中,）
        //if (compareAndSwapTail(pred, node)) {
        //                    pred.next = node;
        //                    return node;
        //                }
        //此时刚好把node换成尾tail,但是还没来得及将pred也就是head的下一个节点指向node，所以导致head.next为空。但是其实已经有另一个节点正在入队了.
        //3：如果排队的首节点就是本节点，则返回false
        return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
    }

    private int fullRelease(Node node) {
        //获取当前持有锁的state
        int state = getState();
        boolean fail = true;
        try {
            if (tryRelease(state)) {
                //释放锁成功
                fail = false;
                return state;
            } else {
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (fail) {
                //无效监视器异常，就会进入此分支,即当前线程不是锁的持有者
                node.waitStatus = Node.CANCELED;
            }
        }
    }

    //是否在同步队列中
    private boolean isOnSyncQueue(Node node) {
        //signal唤醒的时候会将状态设置为0，如果还是CONDITION，则不再同步队列中
        //同步队列是双向链表，condition队列是单向队列，pre前置节点为空，说明还是在condition队列中
        if (node.waitStatus == Node.CONDITION || node.prev == null) {
            return false;
        }
        //节点入队的时候，会首先cas将当前节点的prev指向尾节点，如果成功，再讲尾节点指向自己，如果node的next都有了，肯定在同步队列中了
        if (node.next != null) {
            return true;
        }
        //从同步队列中查找该节点是否在同步队列中
        return findNodeFromTail(node);
    }

    //从同步队列中查找该节点是否在同步队列中
    private boolean findNodeFromTail(Node node) {
        Node t = tail;
        //从尾结点开始，轮询遍历
        for (; ; ) {
            if (t == node) {
                return true;
            }
            if (t == null) {
                return false;
            }
            //对比上一个
            t = t.prev;

        }
    }

    public class MyConditionObject implements MyCondition, Serializable {

        private static final long serialVersionUID = 1173984872572414690L;

        //transient不进行序列化
        private transient Node firstWaiter;

        private transient Node lastWaiter;

        //响应中断
        @Override
        public void await() throws InterruptedException {
            //因为持有锁才能调用，所以不存在线程安全问题
            //如果被中断了，直接抛出中断异常,同时清除中断标记
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Node node = addConditionWaiter();
            //全部释放锁
            int savaState = fullRelease(node);
            //
            int interuptMode = 0;
            //退出循环的条件，1个是当前节点在同步队列中了，另外一种是线程在睡眠途中被中断了
            while (!isOnSyncQueue(node)) {
                //如果不在同步队列中，则睡眠线程
                LockSupport.park(this);
                if ((interuptMode = checkInteruptWhileWaiting(node)) != 0) {
                    //如果在线程睡眠期间被中断了，则停止循环,否则继续判断是否在同步队列中,直到被放到同步队列中
                    break;
                }
            }
            //在同步队列中了，以及重新中断的状态都会入队(解答了之前的why???)，尝试从同步队列中获取锁
            if (acquireQueued(node, savaState) && interuptMode != THROW_IE) {
                //醒了后从同步队列中获取锁，此方法会返回线程的中断状态,
                interuptMode = REINTERRUPT;
            }
            if (node.nextWaiter != null) {
                //signal的时候会将node的nextWaiter置空断开连接，此时为空（signal之后）
                //signal之前中断，只入队，没有将nextWaiter置空，此时扫描需要放弃的节点
                //有节点放弃就需要执行此方法
                unlinkCancelledWaiters();
            }

            if (interuptMode != 0) {
                reportInteruptAfterWait(interuptMode);
            }


        }

        //await()其他的都执行后，处理THROW_IE和REINTERRUPT2种状态
        private void reportInteruptAfterWait(int interuptMode) throws InterruptedException {
            if (interuptMode == THROW_IE) {
                throw new InterruptedException();
            } else if (interuptMode == REINTERRUPT) {
                //重新中断自己
                selfInterrupt();
            }
        }

        //在signal之后中断的，则需要重新中断，
        private static final int REINTERRUPT = 1;
        //在signal之前中断的，则直接抛出异常
        private static final int THROW_IE = -1;

        //等待的时间检测中断的状态
        private int checkInteruptWhileWaiting(Node node) {
            //如果没有中断， 返回0
            //节点被中断分2种情况，一种在signal之前，一种在signal唤醒之后，，，signal唤醒之前则直接抛出中断异常，唤醒之后则重新中断
            return Thread.interrupted() ? (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) : 0;
        }

        //转移
        private boolean transferAfterCancelledWait(Node node) {
            if (compareAndSwapWaitStatus(node, Node.CONDITION, 0)) {
                //如果替换成功，则代表是在signal之前就中断了，因为signal会将此节点状态改为0
                //中断之后依然会转移到阻塞队列（why???）
                enq(node);
                return true;
            }
            while (!isOnSyncQueue(node)) {
                //此时已经被signal方法给放进同步队列的途中了，则暂时释放出执行权，等待放成功
                Thread.yield();
            }
            return false;
        }


        //添加到condition队列中
        private Node addConditionWaiter() {
            Node t = lastWaiter;
            if (t != null && t.waitStatus != Node.CONDITION) {
                //尾节点放弃了，触发清理
                unlinkCancelledWaiters();
                //将t指向新的尾节点
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (firstWaiter == null) {
                //目前队列为空，则fistWaiter为node
                firstWaiter = node;
            } else {
                //将尾节点lastWaiter的下一个指向node
                t.nextWaiter = node;
            }
            //新的尾节点
            lastWaiter = node;
            return node;
        }

        //清理被放弃的节点
        private void unlinkCancelledWaiters() {
            //例子:
            //firstWaiter                                               lastWaiter
            //  Node1    -->  Node2   -->   Node3    -->      Node4  -->Node5
            //Cancel           Cancel      Condition       Cancel        Condition
            //首先Node1为firstWaiter，然后t为Node1,

            //第一次循环
            //next为Node1.next 即Node2
            //Node1为Cancel，所以应该放弃掉,
            //将Node1的nextWaiter置空
            //此节点为放弃，所以不设置trail
            //设置firstWaiter为next，即Node2
            //最后设置t = Node2，开始判断Node2

            //第二次循环
            //next为Node3
            //Node2也就是此时的t也为Cancel，所以也应该放弃掉
            //trail仍然为空，首节点firstWaiter为Node3
            //赋值t为Node3

            //第3次循环
            //因为Node3为CONDITION，所以此时赋值有效的Node3为trail

            //第4次循环
            //Node4为Cancel，trail为Node3
            //将trail.nextWaiter = Node5

            //第5次循环，
            //最后一个为Condition，所以lastWaiter保持不变，
            //如果不为Condition，则lastWaiter = trail;即将最后一个有效的trail赋值为lastWaiter


            //从首节点开始向后扫描，直到没有
            Node t = firstWaiter;
            //  //轨迹节点，用于暂时保存上一个有效的节点
            Node trail = null;
            while (t != null) {
                //获取下一个节点
                Node next = t.nextWaiter;
                //不为CONDITION状态的都应该取消
                if (t.waitStatus != Node.CONDITION) {
                    //将指向下一个节点置空
                    t.nextWaiter = null;
                    if (trail == null) {
                        //轨迹一直为空，只有从头节点开始，后面的节点都属于要被取消的，则会进入此分支
                        firstWaiter = next;
                    } else {
                        //将轨迹（上一个有效的节点）指向下一个
                        trail.nextWaiter = next;
                    }
                    if (next == null) {
                        lastWaiter = trail;
                    }

                } else {
                    //如果此节点不应取消，即为CONDITION，则将此node暂存
                    trail = t;
                }
                //循环判断，此时对next进行判断
                t = next;
            }

        }


        @Override
        public void await(long time, TimeUnit unit) throws InterruptedException {

        }

        @Override
        public void awaitUninterruptibly() {

        }

        @Override
        public void signal() throws InterruptedException {

        }

        @Override
        public void signalAll() throws InterruptedException {

        }
    }


}
