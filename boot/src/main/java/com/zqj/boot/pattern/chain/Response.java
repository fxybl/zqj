package com.zqj.boot.pattern.chain;

import lombok.Data;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.chain
 * @description Response
 * @create 2019-07-03 15:53
 */

@Data
public class Response {

    private String param;

    public Response(String param){
        this.param = param;
    }
}
