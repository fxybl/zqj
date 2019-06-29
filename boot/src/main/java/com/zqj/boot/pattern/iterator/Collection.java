package com.zqj.boot.pattern.iterator;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.iterator
 * @description Collection
 * @create 2019-06-28 17:16
 */
public interface Collection<E> {

    void add(E e);
    int size();
    Iterator iterator();
}
