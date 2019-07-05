package com.zqj.boot.pattern.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.command
 * @description CommandQueue命令队列，多个命令
 * @create 2019-07-05 11:21
 */
public class CommandQueue implements Command {

    private List<Command> queue = new ArrayList<>();

    public void addCommand(Command command){
        queue.add(command);
    }

    public void removeCommand(Command command){
        queue.remove(command);
    }

    @Override
    public void excute() {
        for(Command command: queue){
            command.excute();
        }
    }
}
