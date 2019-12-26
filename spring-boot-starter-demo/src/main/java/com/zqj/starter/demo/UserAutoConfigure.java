package com.zqj.starter.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqj
 * @create 2019-12-26 16:47
 */

@Configuration
@EnableConfigurationProperties(UserProperties.class)
public class UserAutoConfigure {

    @Bean
    @ConditionalOnProperty(prefix = "spring.user",value = "enabled",havingValue = "true")
    //通过其两个属性name(或value)以及havingValue来实现的，其中name用来从application.properties中读取某个属性值。
    //如果该值为空，则返回false;
    //如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。
    //如果返回值为false，则该configuration不生效；为true则生效。
    //
    public UserClient userClient(UserProperties userProperties){
        return new UserClient(userProperties);
    }
}
