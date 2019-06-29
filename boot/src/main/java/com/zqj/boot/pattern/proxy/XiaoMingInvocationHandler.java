package com.zqj.boot.pattern.proxy;

import java.lang.reflect.Method;

/**
 * @author zqj
 * @description XiaoMingInvocationHandler
 * @create 2019-06-27 09:59
 */
public class XiaoMingInvocationHandler implements InvocationHandler {

    private Person person;

    public XiaoMingInvocationHandler(Person person){
        this.person= person;
    }

    @Override
    public void invoke(Object object, Method method) {
        System.out.println("我要开始运动了");
        try {
            method.invoke(person);
        }catch (Exception e){
            System.out.println("原方法执行失败");
        }
        System.out.println("我运动完了");

    }
}
