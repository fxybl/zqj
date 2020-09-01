package com.zqj.service.impl;

import com.zqj.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author zqj
 * @create 2020-09-01 10:17
 */

@Service
public class HelloServiceImpl implements HelloService {

    public String sayHello() {
        return "hello word";
    }
}
