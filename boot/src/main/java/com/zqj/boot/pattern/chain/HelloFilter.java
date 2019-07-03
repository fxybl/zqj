package com.zqj.boot.pattern.chain;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.chain
 * @description HelloFilter
 * @create 2019-07-03 16:10
 */
public class HelloFilter implements Filter {

    @Override
    public void doFilter(Request request, Response response, FilterChain filterChain) {
        String param = request.getParam();
        param= "你好，请求开始----"+param;
        request.setParam(param);
        String param2 = response.getParam();
        param2 = param2+"----请求结束";
        response.setParam(param2);
        filterChain.doFilter(request,response,filterChain);
    }
}
