package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description IntelCPU
 * @create 2019-07-04 17:55
 */
public class IntelCPU implements CPU {

    @Override
    public void xingneng() {
        System.out.println("我是一个性能很6的因特尔CPU");
    }
}
