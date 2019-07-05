package com.zqj.boot.pattern.bridge;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.bridge
 * @description RoundAbstraction扩充抽象类圆形
 * @create 2019-07-05 09:48
 */
public class RoundAbstraction extends Abstraction{

    @Override
    public void draw() {
        implementor.draw();
        System.out.println("画的是圆形");
    }
}
