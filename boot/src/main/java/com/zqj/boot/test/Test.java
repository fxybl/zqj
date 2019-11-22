package com.zqj.boot.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zqj
 * @create 2019-09-19 16:58
 */
public class Test {

    @org.junit.Test
    public void fun(){
        String a = "a";
        String b = "b";
        String c = "c";
        a=b=c;
        System.out.println(a);//c
        System.out.println(b);//c
    }

    @org.junit.Test
    public void fun2() throws Exception{
         Thread thread1 = new Thread(()->{
             try {
                 int i = 0;
                 for(;;){
                     LockSupport.park();
                     Thread.sleep(1000);
                     i++;
                     System.out.println(i);
                     if(i==20){
                         break;
                     }
                 }
             }catch (Exception e){

             }
        });

        thread1.start();

        Thread.sleep(3000);
        LockSupport.unpark(thread1);
        System.out.println("睡眠结束---------------------------------------");
        System.out.println("--------------------------------------------");

    }

    @org.junit.Test
    public void fun3() throws Exception{
        FutureTask<String> futureTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return "s";
            }
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        String result = futureTask.get();
        System.out.println(result);
    }

    @org.junit.Test
    public void fun4(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3,10,0L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(20));
    }

    @org.junit.Test
    public void fun5() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d://a.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //int read = channel.read(byteBuffer);
        //System.out.println(read);
        byteBuffer.flip();
        System.out.println("写东西".getBytes().length);
        byteBuffer.put("写东西".getBytes());
        int write = channel.write(byteBuffer);
        System.out.println(write);
    }

    public static void main(String[] args) throws IOException {
        File f = new File("d://a.txt");
        RandomAccessFile file = new RandomAccessFile(f,"rw");
        FileChannel channel = file.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("你好,123 /r/n".getBytes("UTF-8"));
        byteBuffer.flip();
        channel.write(byteBuffer);
        byteBuffer.clear();
        byteBuffer.put("你好,456 /r/n".getBytes("UTF-8"));
        byteBuffer.flip();
        channel.write(byteBuffer);
        file.close();
        channel.close();

    }



    @org.junit.Test
    public void fun6(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.put(new byte[]{-26, -120, -111, -25, -120, -79, -28, -67, -96});
        byteBuffer.flip();
        Charset charset = Charset.forName("UTF-8");
        CharBuffer decode = charset.decode(byteBuffer);
        char[] chars = Arrays.copyOf(decode.array(), byteBuffer.limit());
        System.out.println(chars);

    }

    @org.junit.Test
    public void fun7(){
       CharBuffer charBuffer = CharBuffer.allocate(128);
       charBuffer.append("世界你好");
       charBuffer.flip();
       Charset set = Charset.forName("GBK");
        ByteBuffer encode = set.encode(charBuffer);
        byte[] bytes = Arrays.copyOf(encode.array(),encode.limit());
        System.out.println(Arrays.toString(bytes));
    }

    @org.junit.Test
    public void fun8() throws  Exception{
        Path path = Paths.get("d://a.txt");
        FileChannel open = FileChannel.open(path);
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)open.size()+1);
        Charset charset = Charset.forName("UTF-8");
        open.read(byteBuffer);
        byteBuffer.flip();
        CharBuffer decode = charset.decode(byteBuffer);
        System.out.println(decode.toString());
        byteBuffer.clear();

    }



}
