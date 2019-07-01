package com.zqj.boot.pattern.strategy;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description Person
 * @create 2019-07-01 16:41
 */
public class Person implements Comparable {

    private int age;

    private int weight;

    public Person(int age,int weight){
        this.age = age;
        this.weight = weight;
    }

    @Override
    public int compareTo(Comparable o) {
        Person p = (Person)o;
        if(this.age>p.age){
            return 1;
        }else if(this.age <p.age){
            return -1;
        }
        if(this.weight>p.weight){
            return 1;
        }else if(this.weight<this.weight){
            return -1;
        }
        return 0;

    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", weight=" + weight +
                '}';
    }

}
