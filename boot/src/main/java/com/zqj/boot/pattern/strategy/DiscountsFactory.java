package com.zqj.boot.pattern.strategy;

import java.util.HashMap;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description DiscountsFactory
 * @create 2019-07-01 17:30
 */
public class DiscountsFactory {

    private static final String FIFTY = "fifty";

    private static final String HUNDRED = "hundred";

    private static final String NONE = "none";

    private static HashMap<String,Discounts> map = new HashMap<>();

    static {
        Discounts fifty = new FiftyDiscounts();
        Discounts hundred = new HundredDiscounts();
        Discounts none = new NoneDiscounts();
        map.put(FIFTY,fifty);
        map.put(HUNDRED,hundred);
        map.put(NONE,none);
    }

    public static Discounts getDiscounts(String key){
       return map.get(key)==null?map.get(NONE):map.get(key);
    }



}
