package com.zqj.boot.pattern.template;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.template
 * @description TemplateChild
 * @create 2019-07-05 15:12
 */
public class TemplateChild extends   TemplateClass{

    @Override
    protected void methodAbstract() {
        System.out.println("我实现了抽象的方法");
    }

    @Override
    protected boolean isPrint() {
        return true;
    }

    @Override
    protected void gouzi() {
        System.out.println("钩子我写了");
    }
}
