package com.zqj.boot.pattern.proxy;

/**
 * @author zqj
 * @description XiaoMing
 * @create 2019-06-27 09:54
 */
public class XiaoMing implements Person {

    @Override
    public void jump() {
        System.out.println("我现在正在跳远");
    }

    @Override
    public void run() {
        System.out.println("我现在正在跑步");
    }
}
