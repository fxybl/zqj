package com.zqj.boot.pattern.command;

import lombok.Setter;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description Invoker调用者即请求发送者，它通过命令对象来执行请求。一个调用者并不
 * 需要在设计时确定其接收者，因此它只与抽象命令类之间存在关联关系。在程序运行时可以
 * 将一个具体命令对象注入其中，再调用具体命令对象的execute()方法，从而实现间接调用请求
 * 接收者的相关操作。
 * @create 2019-07-05 11:20
 */

@Setter
public class Invoker {

    private Command command;

    public Invoker(Command command){
        this.command = command;
    }

    public void call(){
        command.excute();
    }

}
