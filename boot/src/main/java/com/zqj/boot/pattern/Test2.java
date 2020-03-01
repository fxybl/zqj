package com.zqj.boot.pattern;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author zqj
 * @create 2019-08-07 14:21
 */
public class Test2 {


    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Semaphore semaphore = new Semaphore(5);
        final CountDownLatch countDownLatch = new CountDownLatch(1000);
        for(int i=0;i<1000;i++){
            System.out.println("i是"+i);
            executorService.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(10000);
                }catch (Exception e){

                }finally {
                    semaphore.release();
                    countDownLatch.countDown();
                }
            });
        }
        System.out.println("aaaaa");
        countDownLatch.await();
    }


}
