package com.zqj.boot.pattern.factory.simple;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.simple
 * @description SimpleFactory
 * @create 2019-07-04 17:15
 */
public class SimpleFactory {

    public static Person getInstance(String type){
        if("student".equals(type)){
            return new Student();
        }else if("teacher".equals(type)){
            return new Teacher();
        }
        return null;
    }
}
