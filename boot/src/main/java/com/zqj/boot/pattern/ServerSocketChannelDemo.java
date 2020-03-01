package com.zqj.boot.pattern;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @author zqj
 * @create 2019-09-28 17:43
 */
public class ServerSocketChannelDemo {

    public static class TCPEchoServer implements Runnable{

        private InetSocketAddress socketAddress;

        public TCPEchoServer(int port) throws IOException{
            this.socketAddress = new InetSocketAddress(port);
        }

        @Override
        public void run() {
            Random random = new Random();
            Charset charset = Charset.forName("UTF-8");
            ServerSocketChannel ssc =null;
            Selector selector = null;
            try {
                ssc = ServerSocketChannel.open();
                selector = Selector.open();
                ssc.bind(socketAddress);
                ssc.configureBlocking(false);
                ssc.register(selector, SelectionKey.OP_ACCEPT);

            }catch (IOException e){
                System.out.println("server start fail");
                return;
            }

            System.out.println("server is started");
            SelectionKey key = null;
            try {
                while (!Thread.currentThread().isInterrupted()){
                    int n  = selector.select();
                    if(n==0){
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();

                    while (iterator.hasNext()){
                         key = iterator.next();
                        iterator.remove();
                        try{
                            if(key.isAcceptable()){
                                int a = SelectionKey.OP_READ;
                                SocketChannel open = ssc.accept();
                                open.configureBlocking(false);
                                open.register(selector,a,new Buffers(256,256));
                                System.out.println("accept from "+open.getRemoteAddress());
                            }

                            if(key.isReadable()){
                                Buffers buffers = ( Buffers)key.attachment();
                                ByteBuffer read = buffers.getReadBuffer();
                                ByteBuffer write = buffers.getWriteBuffer();
                                SocketChannel socketChannel = (SocketChannel)key.channel();
                                socketChannel.read(read);
                                read.flip();
                                CharBuffer decode = charset.decode(read);
                                System.out.println("收到数据:"+decode.array());
                                read.rewind();
                                write.put("echo message from service".getBytes("UTF-8"));
                                write.put(read);
                                read.clear();
                                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                            }

                            if(key.isWritable()){
                                Buffers  buffers = (Buffers)key.attachment();
                                ByteBuffer write = buffers.getWriteBuffer();
                                write.flip();
                                SocketChannel sc =(SocketChannel)key.channel();
                                int len =0 ;
                                while (write.hasRemaining()){
                                    len = sc.write(write);
                                    if(len==0){
                                        break;
                                    }
                                }
                                write.compact();
                                if(len!=0){
                                    key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                                }


                            }
                        }catch (IOException e){
                            System.out.println("service encounter client error");
                            key.cancel();
                            key.channel().close();
                        }


                    }
                }
                Thread.sleep(random.nextInt(500));
            }catch (Exception e){


            }finally {
                try{
                    selector.close();
                }catch(IOException e){
                    System.out.println("selector close failed");
                }finally{
                    System.out.println("server close");
                }
            }


        }
    }

    public static void main(String[] args) throws Exception{
        Thread thread = new Thread(new TCPEchoServer(8081));
        thread.start();
        Thread.sleep(100000);
        thread.interrupt();
    }
}
