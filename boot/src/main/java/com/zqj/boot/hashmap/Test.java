package com.zqj.boot.hashmap;

/**
 * @author zqj
 * @create 2019-10-28 15:04
 */
public class Test {
    public static void main(String[] args) {
        int h;
        String a = "as";
        int hash = (a == null) ? 0 : (h = a.hashCode()) ^ (h >>> 16);
        System.out.println(hash & 3);
        System.out.println(hash & 7);
        System.out.println(hash & 15);
    }
}
