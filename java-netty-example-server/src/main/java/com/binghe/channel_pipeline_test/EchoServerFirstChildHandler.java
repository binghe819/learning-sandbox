package com.binghe.channel_pipeline_test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerFirstChildHandler implements ChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(EchoServerFirstChildHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.channelRegistered 실행됨");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.channelUnregistered 실행됨");
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.channelActive 실행됨");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.channelInactive 실행됨");
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("EchoServerFirstChildHandler.channelRead 실행됨");
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.channelReadComplete 실행됨");
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("EchoServerFirstChildHandler.userEventTriggered 실행됨");
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.channelWritabilityChanged 실행됨");
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("EchoServerFirstChildHandler.exceptionCaught 실행됨");
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.handlerAdded 실행됨");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerFirstChildHandler.handlerRemoved 실행됨");
    }
}
