package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description IntelMainBoard
 * @create 2019-07-04 17:58
 */
public class IntelMainBoard implements Mainboard {

    @Override
    public void type() {
        System.out.println("我是一块因特尔的主板");
    }
}
