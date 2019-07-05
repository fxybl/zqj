package com.zqj.boot.pattern.template;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.template
 * @description AbstactClass
 * @create 2019-07-05 15:07
 */
public abstract class TemplateClass {

    //模板方法
    public void templateMethod(){
        methodAbstract();
        methodImpl();
        if(isPrint()){
            print();
        }
        gouzi();
    }

    protected void methodImpl(){
        System.out.println("这个方法我实现了的。");
    }


    //抽象方法,子类必须实现
    protected abstract void methodAbstract();


    //钩子方法,子类可写可不写
    protected boolean isPrint(){
        return false;
    }

    //空实现的钩子方法
    protected void gouzi(){

    }

    private void print(){
        System.out.println("开始打印");
    }


}
