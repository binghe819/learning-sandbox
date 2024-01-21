package com.binghe.channel_pipeline_test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerSecondChildHandler implements ChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(EchoServerSecondChildHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.channelRegistered 실행됨");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.channelUnregistered 실행됨");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.channelActive 실행됨");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.channelInactive 실행됨");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("EchoServerSecondChildHandler.channelRead 실행됨");
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.channelReadComplete 실행됨");
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("EchoServerSecondChildHandler.userEventTriggered 실행됨");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.channelWritabilityChanged 실행됨");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("EchoServerSecondChildHandler.exceptionCaught 실행됨");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.handlerAdded 실행됨");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("EchoServerSecondChildHandler.handlerRemoved 실행됨");
    }
}
