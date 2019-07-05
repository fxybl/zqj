package com.zqj.boot.pattern.command;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description Receiver：接收者执行与请求相关的操作，它具体实现对请求的业务处理。
 * @create 2019-07-05 11:02
 */
public interface Receiver {

    void action();
}
