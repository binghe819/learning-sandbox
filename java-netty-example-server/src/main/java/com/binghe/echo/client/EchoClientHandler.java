package com.binghe.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(EchoClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String sendMessage = "Hello Netty";

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());
        log.info("send message: {}", sendMessage);

        ctx.writeAndFlush(messageBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());

        log.info("receive message: {}", readMessage);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception occurred: {}", cause);
        ctx.close();
    }
}
