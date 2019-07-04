package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description Test
 * @create 2019-07-04 17:44
 */
public class Test {

    @org.junit.Test
    public void fun(){
        AmdFactory amdFactory = new AmdFactory();
        CPU cpu = amdFactory.createCPU();
        Mainboard mainboard = amdFactory.createMainboard();
        cpu.xingneng();
        mainboard.type();

        IntelFactory intelFactory = new IntelFactory();
        CPU cpu2 = intelFactory.createCPU();
        Mainboard mainboard2 = intelFactory.createMainboard();
        cpu2.xingneng();
        mainboard2.type();


    }
}
