package com.zqj.boot.pattern.sort;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.sort
 * @description Test
 * @create 2019-06-29 20:32
 */
public class Test {

    @org.junit.Test
    public void fun(){
        int[] a = {5,6,8,1,2,3,7};
        Arrays.print(a);
        Arrays.sort(a,"asc");
        Arrays.print(a);
        Arrays.sort(a,"desc");
        Arrays.print(a);
    }
}
