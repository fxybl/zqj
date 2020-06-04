package com.zqj.boot.netty2.server;

import com.zqj.boot.netty2.center.UserCenter;
import com.zqj.boot.netty2.handler.NewConnectHandler;
import com.zqj.boot.netty2.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 *
 * @author zqj
 * @create 2020-06-04 9:39
 */
public class WebSocketServer {

    private static int port = 8099;

    public static void main(String[] args) throws Exception {
        //用于处理accept()连接的线程池
        EventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
        //用于处理read()等事件的线程池
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //引导器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //注入线程池
            serverBootstrap.group(acceptorGroup,workGroup);
            //注入ReflectiveChannelFactory这个生产Channel的工厂（此时NioServerSocketChannel没有被创建）
            serverBootstrap.channel(NioServerSocketChannel.class);
            //这个参数表示允许重复使用本地地址和端口
            //比如，某个服务器进程占用了TCP的80端口进行监听，此时再次监听该端口就会返回错误，使用该参数就可以解决问题，该参数允许共用该端口，这个在服务器程序中比较常使用
            serverBootstrap.option(ChannelOption.SO_REUSEADDR,true)
                    .childOption(ChannelOption.SO_REUSEADDR,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //入站handler顺序进，出站handler反序出
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new WebSocketServerHandler());
                            pipeline.addLast(new NewConnectHandler());
                        }
                    });
            //bind(int port)绑定端口，此时会由ReflectiveChannelFactory这个工厂创建出jdk的Channel,然后强转为netty的Channel;实质为netty的Channel中注入了jdk的Channel。详见AbstractNioChannel中的SelectableChannel ch
            // 同时设置ServerSocketChannel为非阻塞模式,AbstractNioChannel-->ch.configureBlocking(false);
            //创建成功netty的Channel后， ChannelFuture regFuture = this.config().group().register(channel);通过acceptorGroup线程池异步注册到对应的selector中。
            //此后返回类似JDK的FutureTask的ChannelFuture,可以从中获取注册的结果。
            //sync()阻塞获取注册结果，如果任务失败，将“导致失败的异常”重新抛出来，类似await()
            ChannelFuture future = serverBootstrap.bind(port).sync();
            UserCenter.start();
            //获取通道
            Channel channel = future.channel();
            //获取关闭服务的Future,阻塞监听是否关闭
            channel.closeFuture().sync();


        }finally {
            //优雅的关闭线程池
            acceptorGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
