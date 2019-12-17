package com.zqj.jdk.aqs;

import java.io.Serializable;

/**
 * @author zqj
 * @create 2019-12-06 9:39
 */
public class MyReentrantLock implements Serializable {

    private static final long serialVersionUID = 7373984972572414698L;

    abstract static class Sync extends MyAbstractQueuedSynchronizer {

        private static final long serialVersionUID = 7373984972572414688L;

        abstract void lock();

        //释放锁，公平锁与非公平锁公用一套
        @Override
        protected boolean tryRelease(int args) {

            //释放锁不存在并发问题,因为它已经持有了锁
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                //当前线程不是锁的持有者，则抛出无效监控器状态异常
                throw new IllegalMonitorStateException();
            }
            int state = getState() - args;

            boolean free = false;
            if (state == 0) {
                //如果state变为0了，则表示没有人持有锁了
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(state);
            return free;
        }

        //将非公平锁的获取操作放在sync中,公平锁的单独写在对应的子类FairSync
        protected boolean nonfairTryAcquire(int args) {
            //获取当前线程
            final Thread current = Thread.currentThread();
            //获取状态
            int state = getState();
            if (state == 0) {
                //如果当前没有人持有锁
                //因为是非公平锁，直接尝试cas改变state的值
                if (compareAndSwapState(0, args)) {
                    //更新成功则设置自己为独占锁
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                //如果当前线程就是独占锁线程，因为是可重入锁，则直接state+args
                //因为持有了锁，此时不存在并发操作，可以直接进行设置值
                setState(state + args);
                return true;
            }
            return false;
        }
    }

    //非公平同步锁
    static final class NonfairSync extends Sync {
        @Override
        void lock() {
            //非公平锁直接尝试改变state值
            if(compareAndSwapState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
            }else {
                //失败入队
                acquire(1);
            }
        }

        @Override
        protected boolean tryAcquire(int args) {
            return nonfairTryAcquire(args);
        }
    }

    //公平同步锁
    static final class FairSync extends Sync {

        //争锁
        @Override
        void lock() {
            acquire(1);
        }

        //尝试获取锁
        protected boolean tryAcquire(int args) {
            //获取当前线程
            final Thread current = Thread.currentThread();
            //获取状态
            int state = getState();
            if (state == 0) {
                //如果当前没有人持有锁
                //因为是公平锁，当前线程抢占到了执行权，但是他也要判断自己的前面是否有节点正在等待，如果有，就放弃
                //如果没人等待，则cas更改状态
                if (!hasQueuePred() && compareAndSwapState(0, args)) {
                    //更新成功则设置自己为独占锁
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                //如果当前线程就是独占锁线程，因为是可重入锁，则直接state+args
                //因为持有了锁，此时不存在并发操作，可以直接进行设置值
                setState(state + args);
                return true;
            }
            return false;
        }

    }
}
