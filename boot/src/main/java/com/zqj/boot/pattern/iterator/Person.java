package com.zqj.boot.pattern.iterator;

import lombok.Data;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.iterator
 * @description Person
 * @create 2019-06-29 13:22
 */

@Data
public class Person {

    private String name;
    private  int age;
    public Person(String name,int age){
        this.name = name;
        this.age = age;
    }


}
