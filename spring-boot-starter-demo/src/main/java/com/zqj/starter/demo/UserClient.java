package com.zqj.starter.demo;

/**
 * @author zqj
 * @create 2019-12-26 16:45
 */
public class UserClient {

    private UserProperties userProperties;

    public UserClient(UserProperties userProperties){
        this.userProperties = userProperties;
    }

    public String getName(){
        return userProperties.getName();
    }
}
