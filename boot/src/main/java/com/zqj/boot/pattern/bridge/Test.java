package com.zqj.boot.pattern.bridge;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.bridge
 * @description Test
 * @create 2019-07-04 17:45
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Implementor red = new RedImplementot();
        Implementor green = new GreenImplementor();
        Implementor blue = new BlueImplementor();
        Abstraction shape = new ShapeAbstraction();
        Abstraction round = new RoundAbstraction();
        shape.setImplementor(red);
        shape.draw();
        round.setImplementor(green);
        round.draw();
        round.setImplementor(blue);
        round.draw();
    }
}
