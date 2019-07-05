package com.zqj.boot.pattern.command;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description Test
 * @create 2019-07-04 17:45
 */
public class Test {

    @org.junit.Test
    public void fun(){
        AReceiver ar = new AReceiver();
        Command ac = new ACommand(ar);
        BReceiver br = new BReceiver();
        Command bc  = new BCommand(br);
        Invoker invoker = new Invoker(ac);
        invoker.call();
        invoker.setCommand(bc);
        invoker.call();

        //同时执行几个命令
        CommandQueue commandQueue = new CommandQueue();
        commandQueue.addCommand(ac);
        commandQueue.addCommand(bc);
        InvokerQueue invokerQueue = new InvokerQueue(commandQueue);
        invokerQueue.call();

    }
}
