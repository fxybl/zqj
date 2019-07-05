package com.zqj.boot.pattern.observer;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.observer
 * @description ASubject 订阅号的实现类，具体怎么通知订阅者
 * @create 2019-07-05 14:31
 */
public class ASubject extends Subject {

    @Override
    public void notifyObserver() {
        System.out.println("出故障了，通知维修人员去处理");
        for(Observer observer : observers){
            observer.update();
        }
    }

}
