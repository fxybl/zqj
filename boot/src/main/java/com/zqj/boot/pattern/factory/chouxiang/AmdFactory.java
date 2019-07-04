package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description AmdFactory
 * @create 2019-07-04 18:02
 */
public class AmdFactory extends AbstractFactory {

    @Override
    CPU createCPU() {
        return new AmdCPU();
    }

    @Override
    Mainboard createMainboard() {
        return new AmdMainboard();
    }
}
