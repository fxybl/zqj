package com.zqj.boot.netty;

import io.netty.util.concurrent.*;

/**
 * @author zqj
 * @create 2019-10-23 14:39
 */
public class PromiseTest {

    public static void main(String[] args) {
        DefaultEventExecutor eventExecutors = new DefaultEventExecutor();
        DefaultPromise<Object> objectDefaultPromise = new DefaultPromise<>(eventExecutors);
        objectDefaultPromise.addListener(new GenericFutureListener<Future<? super Object>>() {
            @Override
            public void operationComplete(Future<? super Object> future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("任务执行成功-------任务结果:"+future.get());
                }else {
                    System.out.println("任务执行失败--------异常:"+future.cause());
                }
            }
        }).addListener(new GenericFutureListener<Future<? super Object>>() {
            @Override
            public void operationComplete(Future<? super Object> future) throws Exception {
                System.out.println("任务结束---------");
            }
        });

        eventExecutors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                //objectDefaultPromise.setSuccess(111111);
                objectDefaultPromise.setFailure(new RuntimeException());
            }
        });

        try {
            Promise<Object> sync = objectDefaultPromise.sync();
        } catch (InterruptedException e) {

        }

    }
}
