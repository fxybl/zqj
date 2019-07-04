package com.zqj.boot.pattern.factory.simple;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.simple
 * @description Test
 * @create 2019-07-04 17:19
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Person student = SimpleFactory.getInstance("student");
        student.doSomething();

        Person teacher = SimpleFactory.getInstance("teacher");
        teacher.doSomething();
    }
}
