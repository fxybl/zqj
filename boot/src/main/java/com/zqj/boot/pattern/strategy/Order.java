package com.zqj.boot.pattern.strategy;

import lombok.Data;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description Order
 * @create 2019-07-01 17:13
 */

@Data
public class Order {

    private String userNo;

    private String orderNo;

    //总价，实际开发应使用BigDecimal。此处为了方便
    private Double price;

    private String discountsKey;
}
