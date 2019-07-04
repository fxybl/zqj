package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description AbstractFactory
 * @create 2019-07-04 18:00
 */
public abstract class AbstractFactory {

    abstract Mainboard createMainboard();

    abstract CPU createCPU();


}
