package com.zqj.boot.pattern.proxy;

import java.lang.reflect.Method;

/**
 * @author zqj
 * @description XiaoMingProxy
 * @create 2019-06-27 10:07
 */
public class XiaoMingProxy implements  Person {

    private InvocationHandler handler;

    public XiaoMingProxy(InvocationHandler handler){
        this.handler = handler;
    }

    @Override
    public void jump() {
        try {
            Method method = Person.class.getMethod("jump");
            handler.invoke(this,method);
        }catch (Exception e){
            System.out.println("方法执行异常");
        }

    }

    @Override
    public void run() {
        try {
            Method method = Person.class.getMethod("run");
            handler.invoke(this,method);
        }catch (Exception e){
            System.out.println("方法执行异常");
        }
    }
}
