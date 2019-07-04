package com.zqj.boot.pattern.factory.simple;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.simple
 * @description Student
 * @create 2019-07-04 17:16
 */
public class Student implements Person {

    @Override
    public void doSomething() {
        System.out.println("我会写作业");
    }
}
