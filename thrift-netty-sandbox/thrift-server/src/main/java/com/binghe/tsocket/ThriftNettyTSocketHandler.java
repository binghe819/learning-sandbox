package com.binghe.tsocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class ThriftNettyTSocketHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftNettyTSocketHandler.class);

    private final TProcessor processor;
    private final TProtocolFactory protocolFactory;
    private final ExecutorService executorService;

    public ThriftNettyTSocketHandler(TProcessor processor, TProtocolFactory protocolFactory, ExecutorService executorService) {
        if (executorService == null) {
            throw new IllegalArgumentException("ExecutorService is null");
        }
        this.processor = processor;
        this.protocolFactory = protocolFactory;
        this.executorService = executorService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        ByteBuf output = null;
        boolean submitted = false;

        try {
            output = ctx.alloc().buffer();

            // Task 제출
            executorService.execute(new ThriftNettyTSocketTask(ctx, buffer, output, processor, protocolFactory));
            submitted = true;

        } catch (RejectedExecutionException e) {
            LOGGER.error("Thread pool rejected execution", e);
            ThriftNettyTSocketTask.reject(protocolFactory, ctx, buffer, output, 
                    "Server is busy (RejectedExecutionException)", 
                    TApplicationException.INTERNAL_ERROR);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while submitting task", e);
            ThriftNettyTSocketTask.reject(protocolFactory, ctx, buffer, output, e);
        } finally {
            // Task가 제출되지 않았으면 버퍼 정리
            // 제출된 경우 Task에서 정리함
            if (!submitted) {
                if (buffer != null && buffer.refCnt() > 0) {
                    buffer.release();
                }
                // output은 reject()에서 이미 처리되었거나, 전송되었음
                // 하지만 reject()가 실패했을 수 있으므로 한 번 더 체크
                if (output != null && output.refCnt() > 0) {
                    output.release();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Exception caught in handler", cause);
        // 연결 종료
        ctx.close();
    }
}
