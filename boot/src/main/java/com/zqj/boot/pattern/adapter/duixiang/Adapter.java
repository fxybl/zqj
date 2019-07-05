package com.zqj.boot.pattern.adapter.duixiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.adapter.duixiang
 * @description Adapter适配器可以调用另一个接口，作为一个转换器，对Adaptee和Target进
 * 行适配，适配器类是适配器模式的核心，在对象适配器中，它通过继承Target并关联一个
 * Adaptee对象使二者产生联系。
 * @create 2019-07-05 10:07
 */
public class Adapter implements Target {

    private Adaptee adaptee;

    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        //执行适配进来的方法
        adaptee.specificRequest();
        //执行自己的方法
        System.out.println("我的请求");

    }
}
