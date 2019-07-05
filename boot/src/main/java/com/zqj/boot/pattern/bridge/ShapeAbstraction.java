package com.zqj.boot.pattern.bridge;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.bridge
 * @description RefinedAbstraction扩充抽象类长方形
 * @create 2019-07-05 09:46
 */
public class ShapeAbstraction extends Abstraction {

    @Override
    public void draw() {
        implementor.draw();
        System.out.println("画出的是长方形");
    }
}
