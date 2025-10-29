package com.binghe.tframed;

import com.binghe.ThriftNettySocketHandler;
import com.binghe.processor.CalculatorServiceImpl;
import com.binghe.processor.HealthCheckServiceImpl;
import com.binghe.tsocket.ThriftNettyTSocketApplication;
import com.binghe.tsocket.ThriftTSocketDecoder;
import io.neri.calculator.CalculatorService;
import io.neri.health.HealthService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThriftNettyTFramedSocketApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftNettyTFramedSocketApplication.class);

    public static void main(String[] args) {
        int port = 8080;

        EventLoopGroup bossGroup = new MultiThreadIoEventLoopGroup(
                1,
                new DefaultThreadFactory("neri-acceptor-", true),
                NioIoHandler.newFactory());

        EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(
                Runtime.getRuntime().availableProcessors() * 8,
                new DefaultThreadFactory("neri-worker-", true),
                NioIoHandler.newFactory());

        ExecutorService workerThreadPool = new ThreadPoolExecutor(
                100,
                200,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(600),
                new DefaultThreadFactory("neri-business-worker-"),
                new ThreadPoolExecutor.AbortPolicy()
        );

        TProtocolFactory tProtocolFactory = new TBinaryProtocol.Factory();

        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        processor.registerProcessor(
                "CalculatorService",
                new CalculatorService.Processor<>(new CalculatorServiceImpl())
        );
        processor.registerProcessor(
                "HealthService",
                new HealthService.Processor<>(new HealthCheckServiceImpl())
        );

        ServerBootstrap b = null;

        try {
            b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
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
                                .addLast(new LengthFieldPrepender(4))
                                .addLast(new ThriftNettySocketHandler(processor, tProtocolFactory, workerThreadPool));
                        }
                    });

            // 서버 시작
            LOGGER.info("start server. port: {}", port);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            LOGGER.info("close server. port: {}", port);

            // Business worker pool을 먼저 종료 (진행 중인 작업 완료 대기)
            workerThreadPool.shutdown();
            try {
                if (!workerThreadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                    LOGGER.warn("Worker thread pool did not terminate gracefully within 30 seconds, forcing shutdown");
                    workerThreadPool.shutdownNow();

                    // 강제 종료 후에도 대기
                    if (!workerThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                        LOGGER.error("Worker thread pool did not terminate even after forced shutdown");
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted while waiting for worker thread pool termination", e);
                workerThreadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }

            // Netty event loop groups 종료
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
