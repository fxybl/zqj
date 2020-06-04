package com.zqj.boot.netty2.center;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zqj
 * @create 2020-06-04 18:22
 */
public class UserCenter {

    private static ConcurrentHashMap<String, Channel> userinfos = new ConcurrentHashMap();

    private static final byte[] TEST = new byte[21];


    //保存连接
    public static void saveConnection(String userId, Channel channel) {
        userinfos.put(userId, channel);
    }

    //移除连接
    public static void removeConnection(String userId) {
        if (userId != null) {
            userinfos.remove(userId);
        }
    }

    public static void start() {
        //将zqj拷贝到TEST这个字节数组中
        System.arraycopy("zqj定时为你播报".getBytes(), 0, TEST, 0, 21);
        //开启一个定时的线程池
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
                    try {
                        if (userinfos.size() == 0) {
                            return;
                        }
                        //获取所有的用户ID
                        ConcurrentHashMap.KeySetView<String, Channel> keySetView = userinfos.keySet();
                        String[] keys = keySetView.toArray(new String[]{});
                        int size = keySetView.size();
                        size = size > 10 ? size / 10 : size;
                        for (int i = 0; i < size; i++) {
                            //随机发送
                            String key = keys[new Random().nextInt(size)];
                            Channel channel = userinfos.get(key);
                            if (channel == null) {
                                continue;
                            }
                            if (!channel.isActive()) {
                                userinfos.remove(key);
                                continue;
                            }
                            //获取channel对应的eventLoop去执行操作
                            channel.eventLoop().execute(() -> {
                                channel.writeAndFlush(new TextWebSocketFrame(new String(TEST)));
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                , 2, 2, TimeUnit.SECONDS);

    }


}
