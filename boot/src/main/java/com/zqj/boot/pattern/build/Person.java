package com.zqj.boot.pattern.build;

import lombok.Getter;

/**
 * @author  zqj
 * @create 2019-06-27 21:44
 * @description 建造者模式，年龄和名字必填
 */

@Getter
public class Person {

    private final int age;
    private final String name;
    private final String height;
    private final String weight;
    private final String from;
    private final String sex;

    public static class Builder{
        private final int age;
        private final String name;
        private  String height = "180cm";
        private  String weight = "65kg";
        private  String from = "重庆";
        private  String sex = "男";

        public Builder(int age,String name){
            this.age = age;
            this.name = name;
        }
        public Builder height(String height){
            this.height = height;
            return this;
        }

        public Builder weight(String weight){
            this.weight = weight;
            return this;
        }

        public Builder from(String from){
            this.from = from;
            return this;
        }

        public Builder sex(String sex){
            this.sex = sex;
            return this;
        }

        public Person build(){
            return new Person(this);
        }

    }

    private Person(Builder builder){
        this.age = builder.age;
        this.name = builder.name;
        this.from = builder.from;
        this.height = builder.height;
        this.sex    = builder.sex;
        this.weight= builder.weight;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", from='" + from + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
