package com.zqj.boot.pattern.zhuangshizhe;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.zhuangshizhe
 * @description Test
 * @create 2019-06-27 17:13
 */
public class Test {

    @org.junit.Test
    public void fun(){
        //现在是原始状态
        Person boy = new Boy();
        Person girl = new Girl();
        //被跳舞装饰器包装后
        boy = new DanceDecorator(boy);
        //被唱歌装饰器包装后
        boy = new SingDecorator(boy);
        //就成为全能了
        boy.doSomething();
    }
}
