package com.zqj.boot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.example.echo.EchoClientHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author zqj
 * @create 2019-10-11 9:30
 */
public class Server {

    public static void main(String[] args) throws Exception {
        //用于处理连接请求线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //用于处理请求线程池
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100).handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture f = b.bind(8053).sync();
            f.channel().closeFuture().sync();


        }finally {
           bossGroup.shutdownGracefully();
           workerGroup.shutdownGracefully();
        }


    }
}
