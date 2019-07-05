package com.zqj.boot.pattern.observer;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.observer
 * @description Test
 * @create 2019-07-04 17:45
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Subject subject = new ASubject();
        subject.addObserver(new AObserver());
        subject.addObserver(new BObserver());
        System.out.println("糟了，水管坏了");
        subject.notifyObserver();
    }
}
