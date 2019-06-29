package com.zqj.boot.pattern.proxy;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * author zqj
 * description Proxy
 * create 2019-06-27 10:18
 */
public class Proxy {

    public static Object newInstance(Class clazz,InvocationHandler handler) throws Exception{

        //换行符
        String rt = "\n\r";
        StringBuilder sb = new StringBuilder();
        //遍历此接口的所有方法
        for(Method method : clazz.getMethods()){
            //将abstract字符去掉,获取方法的访问权限字符以及返回参数类型
            String[] part = method.toString().replace("abstract ","").split(" ");
            System.out.println(Arrays.asList(part));
            sb.append("@Override"+rt);
            sb.append(part[0]+" "+part[1]+" "+method.getName()+"(){"+rt);
            sb.append("try{"+rt);
            sb.append("java.lang.reflect.Method method = "+clazz.getName()+".class.getMethod(\""+method.getName()+"\");");
            //this指的是由我们动态生成的代理对象，实际调用的对象是由构造函数传进来的target
            sb.append("handler.invoke(this,method);");
            sb.append("}"+rt);
            sb.append("catch(Exception e){}");
            sb.append("}"+rt);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("package com.zqj.boot.pattern.proxy;"+rt);
        sb2.append("public class Proxy"+clazz.hashCode()+" implements "+clazz.getName()+"{"+rt);
        sb2.append("private com.zqj.boot.pattern.proxy.InvocationHandler handler;"+rt);
        //通过构造器将handler传进来
        sb2.append("public Proxy"+clazz.hashCode()+"(com.zqj.boot.pattern.proxy.InvocationHandler handler){"+rt);
        sb2.append("this.handler= handler;"+rt);
        sb2.append("}"+rt);
        sb2.append(sb);
        sb2.append("}");
        String str = sb2.toString();
        System.out.println(str);
        String path = Proxy.class.getResource("").getFile();
        System.out.println("代理文件路径"+path);
        //创建代理类文件
        File file = new File(path+"Proxy"+clazz.hashCode()+".java");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(str);
        writer.flush();
        writer.close();
        //编译源码，记载class
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileMgr = jc.getStandardFileManager(null, null, null);
        Iterable units = fileMgr.getJavaFileObjects(file);
        JavaCompiler.CompilationTask t = jc.getTask(null, fileMgr, null, null, null, units);
        t.call();
        fileMgr.close();
        //通过URLClassLoader进行加载制定的类
        URL[] urls = new URL[] {new URL("file:/"+path+"Proxy"+clazz.hashCode()+".class")};
        URLClassLoader uc = new URLClassLoader(urls);
        Class myProxy = uc.loadClass("com.zqj.boot.pattern.proxy.Proxy"+clazz.hashCode());
        //获取有handler参数的构造器
        Constructor declaredConstructor = myProxy.getDeclaredConstructor(InvocationHandler.class);
        //传入handler实例并返回生成的动态代理对象
        return declaredConstructor.newInstance(handler);
    }
}
