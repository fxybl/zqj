package com.zqj.boot.pattern.strategy;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description FiftyDiscounts
 * @create 2019-07-01 17:16
 */
public class FiftyDiscounts implements Discounts{

    //满足折扣的价格
    private static final  Double countPrice = 50D;

    //折扣价
    private static final Double limit = 3D;

    @Override
    public void calculate(Order order) {
        if(order.getPrice()>=countPrice){
            System.out.println("使用满50减3折扣券成功");
        }else {
            System.out.println("使用满50减3折扣券失败，不满足条件");
        }

    }
}
