package com.zqj.boot.netty2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zqj
 * @create 2020-06-04 15:13
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final String WEBSOCKET_PATH = "/websocket";

    private WebSocketServerHandshaker handshaker;

    //处理次数，原子Long
    private static final LongAdder counter = new LongAdder();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        counter.add(1);
        if (msg instanceof FullHttpRequest) {
            //如果是http请求
            handlerWebSocketRequest(channelHandlerContext, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketContent(channelHandlerContext, (WebSocketFrame) msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    //初次连接websocket时的处理逻辑
    private void handlerWebSocketRequest(ChannelHandlerContext context, FullHttpRequest request) {
        //解码失败，不是GET请求，不是websocket请求，直接返回失败
        if (!request.decoderResult().isSuccess() || request.method() != HttpMethod.GET || !"websocket".equals(request.headers().get("Upgrade"))) {
            sendHttpResponse(context, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        //构造握手响应
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(getWebsocketPath(request), null, true, 5 * 1024 * 1024);
        handshaker = factory.newHandshaker(request);
        if (handshaker == null) {
            //版本不支持
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
        } else {
            handshaker.handshake(context.channel(), request);
            //继续传播
            context.fireChannelRead(request.retain());
        }


    }

    //发送http响应
    private void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest request, DefaultFullHttpResponse res) {
        if (res.status().code() != 200) {
            //将失败的数据放进ByteBuf缓冲池
            ByteBuf byteBuf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            //获取原本的数据缓冲池
            ByteBuf content = res.content();
            //讲失败的数据写进原来的数据缓冲池
            content.writeBytes(byteBuf);
            //清空并释放失败数据的缓冲池
            byteBuf.release();
            //设置可读的字节数长度
            HttpUtil.setContentLength(res, content.readableBytes());
        }
        //讲数据写进通道,并获取Future
        ChannelFuture future = context.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(request) || res.status().code() != 200) {
            //如果与客户端连接断开，或者不属于我们的协议请求，添加关闭的监听器
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    //发送消息的处理逻辑
    private void handlerWebSocketContent(ChannelHandlerContext context, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(context.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            System.out.println("ping: " + frame);
            context.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            context.channel().write(new TextWebSocketFrame(((TextWebSocketFrame) frame).text() + ",欢迎使用Netty Websocket服务，现在是" + LocalDateTime.now().toString()));
            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {
            //不处理二进制数据
            context.write(frame.retain());
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private String getWebsocketPath(FullHttpRequest req) {
        //IP+"/websocket"
        String location = req.headers().get(HttpHeaderNames.HOST) + WEBSOCKET_PATH;
        return "ws://" + location;
    }
}
