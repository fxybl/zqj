package com.zqj.boot.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * @author zqj
 * @create 2019-11-15 9:43
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.socket().connect(new InetSocketAddress("localhost",8086));
        socketChannel.write(ByteBuffer.wrap("start....".getBytes()));
        ByteBuffer read = ByteBuffer.allocate(1024);
        int num = socketChannel.read(read);
        if(num >0){
            read.flip();
            System.out.println("客户端收取到的数据:"+new String(read.array()));
        }

    }
}
