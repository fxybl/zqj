package com.zqj.boot.pattern.chain;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.chain
 * @description SensitiveFilter
 * @create 2019-07-03 16:07
 */
public class SensitiveFilter implements Filter {

    @Override
    public void doFilter(Request request, Response response, FilterChain filterChain) {
        String param = request.getParam();
        param = param.replace("狗","*");
        param = param.replace("你妹","*");
        request.setParam(param);
        filterChain.doFilter(request,response,filterChain);
    }
}
