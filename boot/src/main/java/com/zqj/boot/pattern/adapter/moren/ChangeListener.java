package com.zqj.boot.pattern.adapter.moren;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.adapter.moren
 * @description ChangeListener
 * @create 2019-07-04 18:32
 */
public class ChangeListener extends EventAdapter {

    @Override
    public void a() {
        System.out.println("实现了a");
    }

    @Override
    public void b() {
        System.out.println("实现了b");
    }
}
