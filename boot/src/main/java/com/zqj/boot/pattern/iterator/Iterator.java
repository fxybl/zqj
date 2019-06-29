package com.zqj.boot.pattern.iterator;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.iterator
 * @description Iterator
 * @create 2019-06-28 17:14
 */
public interface Iterator<E> {

    boolean hasNext();
    E  next();
    boolean hasPrevious();
    E previous();
}
