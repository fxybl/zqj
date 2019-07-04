package com.zqj.boot.pattern.factory.method;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.factory.method
 * @description TeacherFactory
 * @create 2019-07-04 17:39
 */
public class TeacherFactory implements Factory {

    @Override
    public PersonBean getBean() {
        TeacherBean teacher = new TeacherBean();
        teacher.setFrom("火星");
        return teacher;
    }
}
