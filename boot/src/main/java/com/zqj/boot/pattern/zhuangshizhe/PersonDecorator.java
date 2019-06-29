package com.zqj.boot.pattern.zhuangshizhe;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.zhuangshizhe
 * @description PersonDecorator装饰器
 * @create 2019-06-27 17:01
 */
public abstract class PersonDecorator implements Person {

    protected Person person;

    @Override
    public void doSomething() {
        person.doSomething();
    }
}
