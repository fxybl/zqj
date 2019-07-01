package com.zqj.boot.pattern.strategy;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description Hundred
 * @create 2019-07-01 17:26
 */
public class HundredDiscounts implements Discounts{

    //满足折扣的条件
    private static final Double countPrice = 100d;
    //折扣金额
    private static final Double limit = 7d;

    @Override
    public void calculate(Order order) {
        if(order.getPrice()>=countPrice){
            System.out.println("使用满100减7折扣券成功");
        }else {
            System.out.println("使用满100减7折扣券失败");
        }
    }
}
