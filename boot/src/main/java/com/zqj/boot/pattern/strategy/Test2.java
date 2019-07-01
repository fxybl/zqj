package com.zqj.boot.pattern.strategy;

import org.junit.Test;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description Test2
 * @create 2019-07-01 17:40
 */
public class Test2 {

    @Test
    public void fun(){
        Order order = new Order();
        order.setPrice(510d);
        order.setDiscountsKey("fifty");
        DiscountsFactory.getDiscounts(order.getDiscountsKey()).calculate(order);
    }
}
