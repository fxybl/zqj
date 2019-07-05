package com.zqj.boot.pattern.command;

import lombok.Setter;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description InvokerQueue
 * @create 2019-07-05 11:24
 */

@Setter
public class InvokerQueue {

    private CommandQueue commandQueue;

    public  InvokerQueue(CommandQueue commandQueue){
        this.commandQueue = commandQueue;
    }

    public void call(){
        commandQueue.excute();
    }
}
