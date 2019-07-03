package com.zqj.boot.pattern.chain;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.chain
 * @description Test
 * @create 2019-07-03 16:13
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Request request = new Request("你妹哦，你个狗");
        Response response =new Response("不想回你");
        FilterChain filterChain = new FilterChain();
        Filter sensitive = new SensitiveFilter();
        Filter hello = new HelloFilter();
        System.out.println(request.getParam());
        System.out.println(response.getParam());
        filterChain.addFilter(sensitive).addFilter(hello).doFilter(request,response,filterChain);
        System.out.println(request.getParam());
        System.out.println(response.getParam());
    }
}
