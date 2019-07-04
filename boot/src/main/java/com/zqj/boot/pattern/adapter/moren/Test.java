package com.zqj.boot.pattern.adapter.moren;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.adapter.moren
 * @description Test
 * @create 2019-07-04 18:36
 */
public class Test {

    @org.junit.Test
    public void fun(){
        EventListener listener = new ChangeListener();
        listener.a();
        listener.b();

        EventListener listener2 = new ClickListener();
        listener2.e();
        listener2.f();
        listener2.a();
    }
}
