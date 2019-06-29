package com.zqj.boot.pattern.zhuangshizhe;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.zhuangshizhe
 * @description SingDecorator唱歌装饰器
 * @create 2019-06-27 17:09
 */
public class SingDecorator extends PersonDecorator {

    public SingDecorator(Person person){
        super.person = person;
    }

    @Override
    public void doSomething() {
        super.doSomething();
        sing();
    }

    private void sing(){
        System.out.println("我唱歌超6比");
    }
}
