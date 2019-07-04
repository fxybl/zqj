package com.zqj.boot.pattern.factory.chouxiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.chouxiang
 * @description AmdMainboard
 * @create 2019-07-04 17:59
 */
public class AmdMainboard implements Mainboard {

    @Override
    public void type() {
        System.out.println("我是一块AMD的主板啊");
    }
}
