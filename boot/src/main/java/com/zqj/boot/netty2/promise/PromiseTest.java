package com.zqj.boot.netty2.promise;

import io.netty.util.concurrent.*;

/**
 * @author zqj
 * @create 2020-06-04 13:56
 */
public class PromiseTest {

    public static void main(String[] args) {
        //netty线程池
        EventExecutor executor = new DefaultEventExecutor();
        //promise,继承了JDK的Future
        Promise promise = new DefaultPromise(executor);
        //添加监听器
        promise.addListener(new GenericFutureListener<Future<Integer>>() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("执行成功,结果为:" + future.get());
                } else {
                    System.out.println("执行失败");
                }
            }
        });
        promise.addListener(new GenericFutureListener<Future<Integer>>() {
            @Override
            public void operationComplete(Future future) throws Exception {
                System.out.println("执行结束");
            }
        });
        //添加执行的任务，任务执行完成后回调监听器
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    //ignore
                }
                //promise.setSuccess(66666);
                promise.setFailure(new RuntimeException());
            }
        });
        try {
            //主线程阻塞
            promise.sync();
        } catch (InterruptedException e) {
            //ignore
        }

    }
}
