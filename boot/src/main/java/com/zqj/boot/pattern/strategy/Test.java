package com.zqj.boot.pattern.strategy;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description Test
 * @create 2019-07-01 16:50
 */
public class Test {

    @org.junit.Test
    public void fun(){
       Comparable[] comparables = new  Comparable[]{new Person(1,110),new Person(2,110),new Person(3,110),
               new Person(1,120),new Person(1,130),new Person(1,140),new Person(6,100),
               new Person(2,65),new Person(4,99),new Person(3,140),new Person(1,110),};
       DataSort.print(comparables);
       DataSort.sort(comparables);
        System.out.println("---------------------------");
       DataSort.print(comparables);
    }
}
