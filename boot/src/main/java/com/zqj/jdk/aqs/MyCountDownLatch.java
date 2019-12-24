package com.zqj.jdk.aqs;

import java.io.Serializable;

/**
 * @author zqj
 * @create 2019-12-24 16:18
 */
public class MyCountDownLatch {

    private class Sync extends MyAbstractQueuedSynchronizer implements Serializable {

        private static final long seriaVersionUID = 4982264981922014375L;

        Sync(int count) {
            //CountDownLatch只可以使用一次，CyclicBarrier可以反复使用
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int args) {
            //在CountDown中，为0时所有的已完成，可以获取共享锁，为-1不可获取锁。
            return getState() == 0 ? 1 : -1;
        }

        protected boolean tryReleaseShared(int args){
            for( ; ;){
                int state = getState();
                if(state == 0){
                    //已经为0了，不需要再释放共享锁了
                    return false;
                }
                int c = state - 1;
                if(compareAndSwapState(state,c)){
                    //cas更新成功,c不会小于0
                    return c ==0;
                }
            }
        }

    }

    private final Sync sync;

    public MyCountDownLatch(int count){
        if(count <0){
            throw new IllegalArgumentException("count >0");
        }
        this.sync = new Sync(count);
    }

    public void await()throws InterruptedException{
        //获取共享锁（响应中断）
        sync.acquireSharedInterupt(1);
    }


}
