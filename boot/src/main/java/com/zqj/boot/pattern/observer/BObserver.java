package com.zqj.boot.pattern.observer;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.observer
 * @description BObserver
 * @create 2019-07-05 14:33
 */
public class BObserver implements Observer {

    @Override
    public void update() {
        System.out.println("我是B号，我收到消息了，我马上出发");
    }
}
