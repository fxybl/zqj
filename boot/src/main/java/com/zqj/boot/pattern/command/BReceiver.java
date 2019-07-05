package com.zqj.boot.pattern.command;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description BReceiver
 * @create 2019-07-05 11:15
 */
public class BReceiver implements Receiver {

    @Override
    public void action() {
        System.out.println("电视打开了");
    }
}
