package com.zqj.boot.pattern;

import lombok.Data;

import java.nio.ByteBuffer;

/**
 * @author zqj
 * @create 2019-09-28 18:26
 */
@Data
public class Buffers {

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    public Buffers(int read,int write){
        readBuffer = ByteBuffer.allocate(read);
        writeBuffer  = ByteBuffer.allocate(write);
    }
}
