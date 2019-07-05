package com.zqj.boot.pattern.observer;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.observer
 * @description AObserver 实际订阅者，即观察者
 * @create 2019-07-05 14:32
 */
public class AObserver implements Observer {

    @Override
    public void update() {
        System.out.println("我是A号，我收到消息了，我马上出发");
    }
}
