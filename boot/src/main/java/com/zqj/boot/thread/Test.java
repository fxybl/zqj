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

    @org.junit.Test
    public void fun2() throws Exception{
        Person p = new Person();
        //抛出异常
        p.wait();

    }

    @org.junit.Test
    public void fun3(){
        final Animal an = new Animal();
        an.setAge(1);
        an.setName("小狗");
        System.out.println(an);
    }

    @org.junit.Test
    public void fun4(){
        System.out.println(15==0b1111);
        System.out.println(14|0b0001);
    }



}
