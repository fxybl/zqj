package com.zqj.boot.pattern.build;

/**
 * @author  zqj
 * @create 2019-06-27 22:01
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Person.Builder builder = new Person.Builder(18,"阿杰");
        Person person = builder.from("上海").height("178").weight("63").sex("男").build();
        System.out.println(person.toString());
    }
}
