package com.zqj.boot.thread_pool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池工厂
 * @author zqj
 * @create 2020-01-15 11:22
 */
public final class MyExecutors {

    private MyExecutors(){

    }

    /**
     * 无参方法，默认10个核心线程，最大500个线程，无界队列，不做任何处理的拒绝策略
     * @return
     */
    public static ThreadPoolExecutor getThreadPoolExcutor(){
        return new ThreadPoolExecutor(10,500,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),new MyTreadFactory(),new MyIgnorePolicy());
    }

    /**
     *自定义线程池
     * @param corePoolSize 核心线程
     * @param maximumPoolSize  最大线程
     * @param queueNum 有界阻塞队列数
     * @return
     */
    public static ThreadPoolExecutor getThreadPoolExcutor(int corePoolSize,int maximumPoolSize,int queueNum){
        return new ThreadPoolExecutor(corePoolSize,maximumPoolSize,0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(queueNum),new MyTreadFactory(),new MyIgnorePolicy());
    }

    /**
     *
     * 自定义线程池
     * @param corePoolSize
     * @param maximumPoolSize
     * @param queueNum
     * @param handler 自定义拒绝策略，实现RejectedExecutionHandler的rejectedExecution方法
     * @return
     */
    public static ThreadPoolExecutor getThreadPoolExcutor(int corePoolSize,int maximumPoolSize,int queueNum,RejectedExecutionHandler handler){
        return new ThreadPoolExecutor(corePoolSize,maximumPoolSize,0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(queueNum),new MyTreadFactory(),handler);
    }

    /**
     * 自定义线程工厂
     */
    private static class MyTreadFactory implements ThreadFactory {
        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
        }
    }

    /**
     * 自定义拒绝策略
     */
    private static class MyIgnorePolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //不做任何处理
        }
    }
}
