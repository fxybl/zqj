package com.zqj.boot.pattern.factory.method;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.method
 * @description Test
 * @create 2019-07-04 17:40
 */
public class Test {

    @org.junit.Test
    public void fun(){
        StudentFactory studentFactory = new StudentFactory();
        PersonBean studentBean = studentFactory.getBean();
        studentBean.doSomething();

        TeacherFactory teacherFactory = new TeacherFactory();
        PersonBean teacherBean = teacherFactory.getBean();
        teacherBean.doSomething();
    }
}
