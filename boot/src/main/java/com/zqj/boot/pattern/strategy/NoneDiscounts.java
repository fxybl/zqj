package com.zqj.boot.pattern.strategy;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description NoneDiscounts
 * @create 2019-07-01 17:29
 */
public class NoneDiscounts implements Discounts{
    @Override
    public void calculate(Order order) {
        System.out.println("不使用折扣券");
    }
}
