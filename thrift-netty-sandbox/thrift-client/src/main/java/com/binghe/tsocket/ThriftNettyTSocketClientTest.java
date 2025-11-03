package com.binghe.tsocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class ThriftNettyTSocketClientTest {

    public static void main(String[] args) {
        MultiThreadIoEventLoopGroup eventLoopGroup = null;
        Bootstrap bootstrap = null;

        String host = "127.0.0.1";
        int port = 8080;

        try {
            // 스레드 개수는 동일한 목적지에 대한 커넥션을 의미하는가? 아니면 목적지별 하나의 스레드만을 사용하는가?
            eventLoopGroup = new MultiThreadIoEventLoopGroup(
                    10,
                    new DefaultThreadFactory("neri-netty-", true),
                    NioIoHandler.newFactory());

            bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true);
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) {
//                            ch.pipeline()
//                                    // 응답을 받을 핸들러 추가
//                                    .addLast(new ThriftNettyTSocketClientHandler());
//                        }
//                    });

            ChannelFuture connectFuture = bootstrap.connect(host, port);
            Channel channel = connectFuture.channel();


        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (eventLoopGroup != null) {
                eventLoopGroup.shutdownGracefully();
            }
        }

    }
}
