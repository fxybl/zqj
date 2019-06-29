package com.zqj.boot.pattern.singleton;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.singleton
 * @description Singleton饿汉式
 * @create 2019-06-29 16:44
 */
public class Singleton {

    private static final Singleton SINGLETON = new Singleton();

    private Singleton(){}

    public static Singleton getInstance(){
        return SINGLETON;
    }
}
