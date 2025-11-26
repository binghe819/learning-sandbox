package com.binghe.tframedsocket_inflight_equal_1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ThriftNettyTFramedTransportConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ThriftNettyTFramedTransportConnectionManager.class);

    private final String host;
    private final int port;
    private final EventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;
    private final int connectionTimeoutMs;

    public ThriftNettyTFramedTransportConnectionManager(EventLoopGroup eventLoopGroup, int port, String host) {
        this(host, port, eventLoopGroup, 5000);
    }

    public ThriftNettyTFramedTransportConnectionManager(String host, int port, EventLoopGroup eventLoopGroup, int connectionTimeoutMs) {
        this.host = host;
        this.port = port;
        this.eventLoopGroup = eventLoopGroup;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.bootstrap = createBootstrap();
    }

    private Bootstrap createBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMs)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                            // Inbound: Frame decoder (like TFramedTransport) → our handler
                            .addLast(new LengthFieldBasedFrameDecoder(
                                    16 * 1024 * 1024, // maxFrameLength TODO
                                    0, // lengthFieldOffset
                                    4, // lengthFieldLength
                                    0, // lengthAdjustment
                                    4 // initialBytesToStrip (strip the length)))
                            ))
                            // Outbound: prepend length (frame encoder)
                            .addLast(new LengthFieldPrepender(4));
                    }
                });
        return bootstrap;
    }

    public Channel createNewConnection() {
        try {
            logger.info("Connecting to Thrift server at {}:{}", host, port);

            ChannelFuture future = bootstrap.connect(host, port);

            // 연결이 완료될 때까지 대기 (타임아웃 적용)
            if (!future.await(connectionTimeoutMs, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Connection timeout after " + connectionTimeoutMs + "ms");
            }

            if (!future.isSuccess()) {
                throw new RuntimeException("Failed to connect to " + host + ":" + port, future.cause());
            }

            Channel channel = future.channel();
            logger.info("Successfully connected to Thrift server. Channel: {}", channel);

            return channel;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Connection interrupted", e);
        }
    }
}
