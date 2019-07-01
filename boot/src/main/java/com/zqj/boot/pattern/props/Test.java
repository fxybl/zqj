package com.zqj.boot.pattern.props;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.props
 * @description Test
 * @create 2019-07-01 17:55
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Properties properties = new Properties();
        try {
            //获取当前文件的路径
            String path = this.getClass().getResource("").getPath();
            //获取当前类根路径
            String path2 = this.getClass().getResource("/").getPath();
            System.out.println(path);
            System.out.println(path2);

            //相对当前路径
            InputStream in = this.getClass().getResourceAsStream("spring.properties");
            //相对类路径
            InputStream in2 = this.getClass().getClassLoader().getResourceAsStream("com/zqj/boot/pattern/props/spring.properties");
            properties.load(in2);
            String classname = properties.getProperty("people");
            Class clazz = Class.forName(classname);
            Object o = clazz.newInstance();
            Method method = clazz.getMethod("sleep");
            method.invoke(o);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
