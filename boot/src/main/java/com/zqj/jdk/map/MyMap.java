package com.zqj.jdk.map;

/**
 * @author zqj
 * @create 2019-11-25 11:16
 */
public interface MyMap<K,V> {

    V put(K k,V v);

    V get(K k);

    int size();

    interface Entry<K,V>{
        K getKey();

        V getValue();

        void setValue(V v);
    }
}
