package com.zqj.jdk.aqs;

import sun.misc.Unsafe;

import java.io.Serializable;
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
    protected final boolean compareAndSwapNext(Node node, int expect, int update) {
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
        if(status ==Node.SIGNAL){
            return true;
        }
        //>0只有放弃状态，CANCELED,从pred开始，像前遍历，直到找到一个不是>0的节点，然后将此节点指向node
        if(status > 0 ){
            do {
                //pred = pred.prev，将pred赋值为他的上一个节点，因为老的pred放弃了
                //node.prev = pred，
                //直到找到一个不是  CANCELED状态的节点
                node.prev = pred = pred.prev;
            }while (pred.waitStatus >0);

            //找到了,此时返回false,说明不要进行休眠，继续上一个方法acquireQueued（）的轮询
            pred.next = node;
        }else {
            //状态为0,即初始状态，或者为PROPAGATE(为啥不为CONDITION?待看了Condition再来分析)
            //将状态设置为SIGNAL,不管成功与否，都返回false,重新走一遍acquireQueued（）的轮询...如果设置成功，则会进入本方法第一个if,返回true。
            compareAndSwapWaitStatus(pred,status,Node.SIGNAL);
        }
        return false;
    }

    private void cancelAcquire(Node node) {
        if(node == null){
            return;
        }
        node.thread = null;
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
    }


}