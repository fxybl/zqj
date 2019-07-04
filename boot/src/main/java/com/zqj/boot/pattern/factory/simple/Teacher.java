package com.zqj.boot.pattern.factory.simple;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.simple
 * @description Teacher
 * @create 2019-07-04 17:17
 */
public class Teacher implements Person {

    @Override
    public void doSomething() {
        System.out.println("我会教书，6不6");
    }
}
