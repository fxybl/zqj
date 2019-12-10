package com.zqj.jdk.aqs;

import java.io.Serializable;

/**
 * @author zqj
 * @create 2019-12-06 9:39
 */
public class MyReentrantLock implements Serializable {

    private static final long serialVersionUID = 7373984972572414698L;

    abstract static class Sync extends MyAbstractQueuedSynchronizer{

        private static final long serialVersionUID = 7373984972572414688L;

       abstract void lock();

        @Override
        protected boolean tryRelease(int args) {
            return false;
        }

        @Override
        protected boolean tryAcquire(int args) {
            return false;
        }
    }

    //非公平同步锁
    static final class NonfairSync extends Sync{
        @Override
        void lock() {

        }
    }

    //公平同步锁
    static final class FairSync extends Sync{
        @Override
        void lock() {

        }
    }
}
