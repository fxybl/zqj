package com.zqj.boot.pattern.chain;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.chain
 * @description Filter
 * @create 2019-07-03 15:53
 */
public interface Filter {

    void doFilter(Request request,Response response,FilterChain filterChain);
}
