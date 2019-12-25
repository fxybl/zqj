package com.zqj.jdk.aqs;

import java.io.Serializable;

/**
 * @author zqj
 * @create 2019-12-25 19:03
 */
public class MySemaphore implements Serializable {

    private final static long serialVersionUID = 3222578661600680211L;

    private final Sync sync;

    abstract static class Sync extends MyAbstractQueuedSynchronizer {

        private static final long serialVersionUID = 1192457210091910935L;

        Sync(int permits) {
            setState(permits);
        }

        //非公平方式获取锁
        final int nonfairTryAcquireShared(int args) {
            //当有可用数量时,轮询获取
            for (; ; ) {
                int state = getState();
                int c = state - args;
                if (c < 0 || compareAndSwapState(state, c)) {
                    //1:
                    // 如果减去后的数量刚好等于0获取大于0，说明是获取了锁了，此时第一个c<0条件不满足，则会进入第2个条件
                    //然后cas改变state，如果成功，直接返回剩余的数量
                    //如果cas失败，说明其他线程抢先了，则重新轮询
                    //2:
                    //如果减去后的数量直接小于0，说明没获取到锁，则直接返回，即负数
                    return c;
                }
            }
        }

        protected final boolean tryReleaseShared(int args) {
            for (; ; ) {
                int state = getState();
                int c = state + args;
                if (c < state) {
                    throw new Error("初始化值设置有误");
                }
                if (compareAndSwapState(state, c)) {
                    //设置成功，返回true
                    return true;
                }
            }
        }
    }

    //非公平
    static final class NonfairSync extends Sync {

        NonfairSync(int permits) {
            super(permits);
        }

        protected int tryAcquireShared(int args) {
            return nonfairTryAcquireShared(args);
        }
    }

    //公平
    static final class FairSync extends Sync {

        FairSync(int permits) {
            super(permits);
        }

        protected int tryAcquireShared(int args) {
            for (; ; ) {
                if (hasQueuePred()) {
                    //如果前面有人在排队，直接返回-1，-1代表获取锁失败
                    return -1;
                }
                int state = getState();
                int c = state - args;
                if (c < 0 || compareAndSwapState(state, c)) {
                    return c;
                }
            }
        }
    }

    public MySemaphore(int permits) {
        //默认是非公平锁
        sync = new NonfairSync(permits);
    }

    private MySemaphore(int permits, boolean fair) {
        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
    }

    //MySemaphore ms = new MySemaphore(10);

    //try{
    //     ms.acquire();
    // }finally{
    //      ms.release();
    // }

    //获取锁
    public void acquire() throws InterruptedException {
        sync.acquireSharedInterupt(1);
    }


    //释放锁
    public void release() {
        sync.releaseShared(1);
    }
}
