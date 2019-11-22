package com.zqj.boot.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zqj
 * @create 2019-08-19 10:43
 */
public class Test {

    @org.junit.Test
    public void fun() throws Exception{
        FileInputStream fileInputStream = new FileInputStream(new File("e://a.txt"));
        FileChannel channel = fileInputStream.getChannel();
       /* ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        System.out.println(buffer);
        System.out.println(read);*/
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
        buffer2.put("nihao".getBytes());
        while (buffer2.hasRemaining()){
            channel.write(buffer2);
        }

    }
}
