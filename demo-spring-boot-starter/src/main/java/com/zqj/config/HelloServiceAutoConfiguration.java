package com.zqj.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqj
 * @create 2020-09-01 10:18
 */

@Configuration
@ComponentScan(basePackages = {"com.zqj"})
@ConditionalOnProperty(prefix = "hello",name = "enable",havingValue = "true")
public class HelloServiceAutoConfiguration {
}
