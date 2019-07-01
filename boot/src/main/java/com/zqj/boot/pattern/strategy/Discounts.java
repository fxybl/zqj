package com.zqj.boot.pattern.strategy;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description Discounts
 * @create 2019-07-01 17:12
 */
public interface Discounts {
    //计算
    void calculate(Order order);

}
