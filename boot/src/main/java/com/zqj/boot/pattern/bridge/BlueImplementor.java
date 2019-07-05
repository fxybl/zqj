package com.zqj.boot.pattern.bridge;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.bridge
 * @description BlueImplementor
 * @create 2019-07-05 09:40
 */
public class BlueImplementor implements Implementor {

    @Override
    public void draw() {
        System.out.println("用蓝色笔画的");
    }
}
