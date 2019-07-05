package com.zqj.boot.pattern.command;


/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description Command抽象命令类一般是一个抽象类或接口，在其中声明了用于执行
 * 请求的execute()等方法，通过这些方法可以调用请求接收者的相关操作。
 * @create 2019-07-05 11:05
 */

public  interface Command {

    void excute();
}
