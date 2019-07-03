package com.zqj.boot.pattern.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.chain
 * @description FilterChain
 * @create 2019-07-03 15:54
 */
public class FilterChain implements Filter {

    private int index = 0;

    private List<Filter> filters = new ArrayList<>();


    public FilterChain addFilter(Filter filter){
            filters.add(filter);
            return this;
    }
    @Override
    public void doFilter(Request request, Response response,FilterChain filterChain) {
        if(index==filters.size()){
            return;
        }
        Filter filter = filters.get(index);
        index++;
        filter.doFilter(request,response,filterChain);
    }
}
