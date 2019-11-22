package com.zqj.boot.netty.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zqj
 * @create 2019-11-21 14:31
 */

@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel income = ctx.channel();
        for(Channel channel : channels){
            channel.writeAndFlush("[SERVER ]-"+channel.remoteAddress()+" 加入  \n");
        }
        channels.add(income);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel outcome = ctx.channel();
        for(Channel channel : channels){
            channel.writeAndFlush("【SERVER 】 -" +channel.remoteAddress()+" 退出   \n");
        }
        channels.remove(outcome);
        outcome.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel income = ctx.channel();
        income.writeAndFlush("【you received】"+s+"\n");
        for(Channel channel : channels){
            if(channel != income){
                channel.writeAndFlush("【SERVER】-"+income.remoteAddress()+" received"+s+"\n");
            }
        }


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channel.writeAndFlush("[SERVER] -"+ channel.remoteAddress()+" 在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel= ctx.channel();
        channel.writeAndFlush("[SERVER] -"+ channel.remoteAddress()+" 离线");
        channels.remove(channel);
        channel.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        log.error("异常原因：{}",cause);
        channel.close();

    }
}
