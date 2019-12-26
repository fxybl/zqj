package com.zqj.starter.demo;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zqj
 * @create 2019-12-26 16:37
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({UserAutoConfigure.class})
public @interface EnableUserClient {

}
