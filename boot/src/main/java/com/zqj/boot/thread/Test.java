package com.zqj.boot.thread;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.thread
 * @description Test
 * @create 2019-07-09 17:08
 */
public class Test {

    //一个对象一个monitor监视器，使用synchronized同步代码块时，调用另一个对象的wait,notify等，会抛出异常，无效的监视器状态异常
    @org.junit.Test
    public void fun() throws Exception{
        Person p =  new Person();
        Animal a = new Animal();
        synchronized (p){
            //抛异常
            a.wait();
            //正常
            p.wait();
        }
    }

}
