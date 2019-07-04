package com.zqj.boot.pattern.factory.method;

import lombok.Data;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.method
 * @description StudentBean
 * @create 2019-07-04 17:27
 */

@Data
public class StudentBean implements PersonBean {

    private String name;

    @Override
    public void doSomething() {
        System.out.println("我叫"+name);
        System.out.println("我上能写字，下能读书");
    }
}
