package com.zqj.boot.pattern.command;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description BCommand
 * @create 2019-07-05 11:12
 */
public class BCommand implements Command {

    private BReceiver receiver;

    public BCommand(BReceiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void excute() {
        System.out.println("我想把电视打开");
        receiver.action();
    }
}
