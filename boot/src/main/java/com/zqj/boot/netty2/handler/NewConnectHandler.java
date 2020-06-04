package com.zqj.boot.netty2.handler;

import com.zqj.boot.netty2.center.UserCenter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;

import java.util.List;
import java.util.Map;

/**
 * @author zqj
 * @create 2020-06-04 15:12
 */
public class NewConnectHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        Map<String, List<String>> parameters = new QueryStringDecoder(request.uri()).parameters();
        String userId = parameters.get("userId").get(0);
        channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).getAndSet(userId);
        UserCenter.saveConnection(userId,channelHandlerContext.channel());
    }
}
