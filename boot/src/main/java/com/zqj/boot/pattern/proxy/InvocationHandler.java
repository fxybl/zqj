package com.zqj.boot.pattern.proxy;

import java.lang.reflect.Method;

/**
 * @author zqj
 * @description InvocationHandler
 * @create 2019-06-27 09:55
 */
public interface InvocationHandler {

    void invoke(Object o, Method method);

}
