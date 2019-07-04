package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description IntelFactory
 * @create 2019-07-04 18:01
 */
public class IntelFactory extends AbstractFactory {

    @Override
    Mainboard createMainboard() {
        return new IntelMainBoard();
    }

    @Override
    CPU createCPU() {
        return new IntelCPU();
    }
}
