package com.zqj.boot.pattern.bridge;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.bridge
 * @description AImplementot
 * @create 2019-07-05 09:36
 */
public class RedImplementot implements  Implementor{

    @Override
    public void draw() {
        System.out.println("用红色画笔画的");
    }
}
