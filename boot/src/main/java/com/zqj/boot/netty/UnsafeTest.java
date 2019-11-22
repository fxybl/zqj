package com.zqj.boot.netty;

import sun.misc.Unsafe;

/**
 * @author zqj
 * @create 2019-10-23 15:34
 */
public class UnsafeTest {
    public static void main(String[] args) {
        Unsafe unsafe = Unsafe.getUnsafe();
        System.out.println(unsafe);
    }
}
