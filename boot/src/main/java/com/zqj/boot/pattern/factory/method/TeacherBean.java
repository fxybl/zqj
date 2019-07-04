package com.zqj.boot.pattern.factory.method;

import lombok.Data;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.method
 * @description TeacherBean
 * @create 2019-07-04 17:28
 */

@Data
public class TeacherBean implements PersonBean {

    private String from;

    @Override
    public void doSomething() {
        System.out.println("我是一名教师，我来自"+from);
        System.out.println("我上能教书，下能做饭");
    }
}
