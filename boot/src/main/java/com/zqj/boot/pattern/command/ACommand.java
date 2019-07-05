package com.zqj.boot.pattern.command;

import lombok.Setter;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description ACommand打开灯的命令
 * @create 2019-07-05 11:09
 */

@Setter
public class ACommand implements Command {

    private AReceiver receiver;

    public ACommand(AReceiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void excute() {
        System.out.println("我想把灯打开");
        receiver.action();
    }
}
