package com.binghe.channel_pipeline_test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerInboundHandler implements ChannelInboundHandler {
    
    private static final Logger log = LoggerFactory.getLogger(EchoServerInboundHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstHandler.channelRegistered 실행됨");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstHandler.channelUnregistered 실행됨");
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstHandler.channelActive 실행됨");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstHandler.channelInactive 실행됨");
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("EchoServerFirstHandler.channelRead 실행됨");
//        ctx.write(msg);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstHandler.channelReadComplete 실행됨");
//        ctx.flush();
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("EchoServerFirstHandler.userEventTriggered 실행됨");
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstHandler.channelWritabilityChanged 실행됨");
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("EchoServerFirstHandler.exceptionCaught 실행됨");
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // Bootstrap 동작시 handler ChannelPipeline에 추가되면서 실행됨.
        log.info("EchoServerFirstHandler.handlerAdded 실행됨");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // Bootstrap 종료시 handler ChannelPipeline에 추가되면서 실행됨.
        log.info("EchoServerFirstHandler.handlerRemoved 실행됨");
    }
}
