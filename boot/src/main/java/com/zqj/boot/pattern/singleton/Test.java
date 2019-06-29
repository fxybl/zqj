package com.zqj.boot.pattern.singleton;

/**
 * @author  zqj
 * @program com.zqj.boot.pattern.singleton
 * @description Test
 * @create 2019-06-29 17:05
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Singleton singleton = Singleton.getInstance();
        Singleton singleton2 = Singleton.getInstance();
        System.out.println(singleton.hashCode());
        System.out.println(singleton2.hashCode());
    }

    @org.junit.Test
    public void fun2(){
        Singleton2 singleton = Singleton2.getInstance();
        Singleton2 singleton2 = Singleton2.getInstance();
        System.out.println(singleton.hashCode());
        System.out.println(singleton2.hashCode());
    }

    @org.junit.Test
    public void fun3(){
        Singleton3 singleton = Singleton3.SINGLETON;
        Singleton3 singleton2 = Singleton3.SINGLETON;
        System.out.println(singleton.hashCode());
        System.out.println(singleton2.hashCode());
    }


}
