package com.zqj.boot.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zqj
 * @create 2019-11-15 9:43
 */
public class Server {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(8086));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            int select = selector.select();
            if(select==0){
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer read = ByteBuffer.allocate(1024);
                    int num ;
                    try{
                        num = socketChannel.read(read);
                    }catch (IOException e){
                        System.out.println("socket连接异常了");
                        key.cancel();
                        socketChannel.close();
                        break;
                    }
                    if(num >0){
                        System.out.println("收到数据:"+new String(read.array()).trim());
                        ByteBuffer write = ByteBuffer.wrap("返回给客户端数据.....".getBytes());
                        socketChannel.write(write);
                    }else if(num == -1){
                        socketChannel.close();
                    }
                }
            }
        }

    }
}
