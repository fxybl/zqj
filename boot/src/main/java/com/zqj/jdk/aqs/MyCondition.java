package com.zqj.jdk.aqs;

import java.util.concurrent.TimeUnit;

/**
 * @author zqj
 * @create 2019-12-09 17:38
 */
public interface MyCondition {

    //响应中断
    void await() throws InterruptedException;

    void signal() throws InterruptedException;

    void signalAll() throws InterruptedException;

    //不响应中断
    void awaitUninterruptibly();

    //超时取消等待
    void await(long time, TimeUnit unit)throws InterruptedException;
}
