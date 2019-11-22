package com.zqj.boot.cas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zqj
 * @program com.zqj.boot.cas
 * @description Test
 * @create 2019-07-10 16:07
 */
public class Test {

    //AtomicInteger有一个compareAndSet方法，有两个操作数，第一个是期望值，第二个是希望修改成的值。
    // 首先初始值是5，第一次调用compareAndSet方法的时候，将5拷贝回自己的工作空间，然后改成50，
    // 写回到主内存中的时候，它期望主内存中的值是5，而这时确实也是5，所以可以修改成功，主内存中的值也变成了50，输出true。
    // 第二次调用compareAndSet的时候，在自己的工作内存将值修改成100，写回去的时候，希望主内存中的值是5，
    // 但是此时是50，所以set失败，输出false。这就是比较并交换，也即CAS。

    @org.junit.Test
    public void fun(){
        AtomicInteger atomicInteger = new AtomicInteger(5);
        //打印true,即修改成功,V和A的值相等，新值B修改成功
        System.out.println(atomicInteger.compareAndSet(5,50));
        //打印false,即修改失败，V和A值不等，新值B修改失败
        System.out.println(atomicInteger.compareAndSet(5,100));
        AtomicReference<Integer> atomic = new AtomicReference<>(5);
        //打印true,即修改成功,V和A的值相等，新值B修改成功
        System.out.println(atomic.compareAndSet(5,50));
        //打印false,即修改失败，V和A值不等，新值B修改失败
        System.out.println(atomic.compareAndSet(5,100));



    }
}
