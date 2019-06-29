package com.zqj.boot.pattern.proxy;

/**
 * @author zqj
 * @description Test
 * @create 2019-06-27 10:29
 */
public class Test {

    @org.junit.Test
    public void fun(){
        XiaoMingProxy xiaoMingProxy = new XiaoMingProxy(new XiaoMingInvocationHandler(new XiaoMing()));
        xiaoMingProxy.jump();
        xiaoMingProxy.run();
    }

    @org.junit.Test
    public void fun2() throws Exception{
        Person person = (Person)Proxy.newInstance(Person.class,new XiaoMingInvocationHandler(new XiaoMing()));
        person.jump();
        person.run();
    }
}
