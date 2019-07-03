package com.zqj.boot.pattern.chain;

import lombok.Data;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.chain
 * @description Request
 * @create 2019-07-03 15:50
 */

@Data
public class Request {

    private String param;

    public Request(String param){
        this.param = param;
    }
}
