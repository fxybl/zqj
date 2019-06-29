package com.zqj.boot.pattern.zhuangshizhe;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.zhuangshizhe
 * @description DanceDecorator跳舞包装器
 * @create 2019-06-27 17:03
 */
public class DanceDecorator extends PersonDecorator {

    public DanceDecorator(Person person){
       super.person= person;
    }

    @Override
    public void doSomething() {
        super.doSomething();
        dance();

    }

    private void dance(){
        System.out.println("我跳舞很吊的");
    }
}
