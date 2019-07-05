package com.zqj.boot.pattern.command;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description AReceiver
 * @create 2019-07-05 11:11
 */
public class AReceiver implements Receiver {

    @Override
    public void action() {
        System.out.println("灯打开了");
    }
}
