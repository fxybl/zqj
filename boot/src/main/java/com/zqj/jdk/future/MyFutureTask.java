package com.zqj.jdk.future;

/**
 * @author zqj
 * @create 2019-11-28 11:09
 */


import sun.misc.Unsafe;

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

    public MyFutureTask(MyCallable callable){
        if(callable==null){
            throw new NullPointerException();
        }
        this.callable = callable;
        this.state = NEW;
    }

    @Override
    public void run() {
        if(state !=NEW || !UNSAFE.compareAndSwapObject(this,runnerOffset,null,Thread.currentThread())){
            //如果此时状态不是NEW,或者改变当前持有线程的时候失败，则直接返回。这样保证只有一个线程在执行此方法（因为会进行2个非原子性操作，1个new对象时改变state为NEW，一个run时改变持有的线程。）
            return;
        }
        try{
            //用另一个指针指向callable对象
            MyCallable<V> callable = this.callable;

            if(callable !=null && state ==NEW){
                //运行结果
                boolean ran;
                V result;
                try{
                    result = callable.call();
                    ran= true;

                }catch (Exception e){
                    result = null;
                    ran = false;
                    setException(e);
                }
                if(ran){
                    set(result);
                }
            }

        }finally {
            //线程执行完则取消当前线程的绑定
            runner = null;
        }



    }

    //执行成功设置值
    private void set(V result) {

    }

    //callable执行失败设置异常
    private void setException(Exception e) {
        //尝试改变state为completing
        if(UNSAFE.compareAndSwapInt(this,stateOffset,NEW,COMPLETING)){
            //改变成功后，设置结果
            outcome = e;
            //设置后，强制改变state为异常的状态
            UNSAFE.putOrderedInt(this,stateOffset,EXCEPTIONAL);
            //改变后唤醒通知get()时所有休眠的线程，通知他们已经有结果了
            finishCompletion();
        }
    }

    //完成之后唤醒那些休眠的线程去获取结果了
    private void finishCompletion() {
        //遍历waiters这个链状结构的等待队列;

        for(WaiterNode w;(w=waiters)!=null ;){

        }

    }

    //等待链表队列节点
    static final class WaiterNode {
        //等待的线程
        private volatile Thread thread;
        //下一个节点
        private volatile WaiterNode next;


        WaiterNode(Thread thread) {
            this.thread = thread;
        }
    }
}
