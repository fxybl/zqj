package com.zqj.boot.pattern.adapter.duixiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.adapter.duixiang
 * @description MyAdaptee
 * @create 2019-07-05 10:16
 */
public class MyAdaptee implements Adaptee {

    @Override
    public void specificRequest() {
        System.out.println("我的特别处理");
    }
}
