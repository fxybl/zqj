package com.zqj.common.resp;

import lombok.Data;

/**
 * @author zqj
 * @create 2019-12-26 18:12
 */

@Data
public class Result<T> {

    private int code =200;

    private String message;

    private T data;


}
