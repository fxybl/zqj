package com.zqj.starter.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zqj
 * @create 2019-12-26 16:28
 */

@Data
@ConfigurationProperties("spring.user")
public class UserProperties {

    private String name;
}
