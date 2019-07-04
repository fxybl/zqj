package com.zqj.boot.pattern.factory.method;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.method
 * @description StudentFactory
 * @create 2019-07-04 17:36
 */
public class StudentFactory implements Factory {

    @Override
    public PersonBean getBean() {
        StudentBean student = new StudentBean();
        student.setName("张三");
        return student;
    }
}
