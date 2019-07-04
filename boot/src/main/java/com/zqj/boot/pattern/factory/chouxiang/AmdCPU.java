package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description AmdCPU
 * @create 2019-07-04 17:56
 */
public class AmdCPU implements CPU {

    @Override
    public void xingneng() {
        System.out.println("我是一块很秀的AMD的CPU");
    }
}
