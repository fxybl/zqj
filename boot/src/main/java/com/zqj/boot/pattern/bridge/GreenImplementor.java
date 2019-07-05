package com.zqj.boot.pattern.bridge;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.bridge
 * @description GreenImplementor
 * @create 2019-07-05 09:38
 */
public class GreenImplementor implements Implementor {

    @Override
    public void draw() {
        System.out.println("用绿色画笔画的");
    }
}
