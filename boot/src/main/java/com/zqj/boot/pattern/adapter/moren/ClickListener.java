package com.zqj.boot.pattern.adapter.moren;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.adapter.moren
 * @description ClickListener
 * @create 2019-07-04 18:34
 */
public class ClickListener extends EventAdapter {

    @Override
    public void e() {
        System.out.println("实现了e");
    }

    @Override
    public void f() {
        System.out.println("实现了f");
    }
}
