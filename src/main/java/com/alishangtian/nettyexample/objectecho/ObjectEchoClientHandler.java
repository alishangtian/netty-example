package com.alishangtian.nettyexample.objectecho;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handler implementation for the object echo client.  It initiates the ping-pong traffic between the object echo client
 * and server by sending the first message to the server.
 */
@ChannelHandler.Sharable
public class ObjectEchoClientHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ObjectEchoClientHandler.class);
    private final AtomicInteger nums = new AtomicInteger();
    private ArrayListMultimap<String, Channel> activeChannel = ArrayListMultimap.create();
    private Bootstrap bootstrap;

    /**
     * Creates a client-side handler.
     */
    public ObjectEchoClientHandler() {
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void initPool(int size) {
        for (int i = 0; i < size; i++) {
            try {
                bootstrap.connect("127.0.0.1", 8007).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Channel getChannel(String addr) {
        return activeChannel.get(addr).get(0);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        activeChannel.put(ctx.channel().remoteAddress().toString(), ctx.channel());
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                ctx.channel().writeAndFlush(new Message("ping"));
//            }
//        }, 10 * 1000, 10 * 1000);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info("msg from server,address:{},msg:{}", ctx.channel().remoteAddress(), JSON.toJSONString(msg));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
