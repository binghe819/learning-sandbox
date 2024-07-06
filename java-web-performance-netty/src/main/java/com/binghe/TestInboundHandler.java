package com.binghe;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestInboundHandler implements ChannelInboundHandler {

    private final Logger log = LoggerFactory.getLogger(TestInboundHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("channelRegistered executed!");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("channelUnregistered executed!");
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("channelActive executed!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("channelInactive executed!");
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info("channelRead executed!");
        log.info("received data : {}", o);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("channelReadComplete executed!");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info("userEventTriggered executed!");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("channelWritabilityChanged executed!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        log.info("exceptionCaught executed!: {}", throwable);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("handlerAdded executed!");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("handlerRemoved executed!");
    }
}
