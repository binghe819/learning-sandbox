package com.binghe;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftNettySocketProcessTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftNettySocketProcessTask.class);

    private final ChannelHandlerContext ctx;
    private final ByteBuf input;
    private final ByteBuf output;
    private final TProcessor processor;
    private final TProtocolFactory protocolFactory;

    public ThriftNettySocketProcessTask(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output, TProcessor processor, TProtocolFactory protocolFactory) {
        this.ctx = ctx;
        this.input = input;
        this.output = output;
        this.processor = processor;
        this.protocolFactory = protocolFactory;
    }

    public static void process(TProcessor processor, TProtocolFactory protocolFactory, ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) {
        boolean writtenSuccessfully = false;
        try {
            if (output == null) {
                // Response Buffer
                output = ctx.alloc().buffer();
            }
            
            // Transport
            TNettyChannelTransport transport = new TNettyChannelTransport(input, output);
            
            // Protocol
            var inputProtocol = protocolFactory.getProtocol(transport);
            var outputProtocol = protocolFactory.getProtocol(transport);
            
            // process
            processor.process(inputProtocol, outputProtocol);
            
            // write response
            ctx.writeAndFlush(output);
            writtenSuccessfully = true;
            LOGGER.debug("Successfully processed Thrift request");
        } catch (Exception e) {
            LOGGER.error("Error processing Thrift request", e);
            
            // output clear when error occurred
            if (output != null && output.refCnt() > 0) {
                output.clear();
            }
            
            // send error
            reject(protocolFactory, ctx, input, output, e);
        } finally {
            // clear input buffer
            if (input != null && input.refCnt() > 0) {
                input.release();
            }
            if (!writtenSuccessfully && output != null && output.refCnt() > 0) {
                output.release();
            }
        }
    }

    public static void reject(TProtocolFactory protocolFactory,
                              ChannelHandlerContext ctx,
                              ByteBuf input,
                              ByteBuf output,
                              Exception e) {
        reject(protocolFactory, ctx, input, output, e.getMessage(), getErrorType(e));
    }

    public static void reject(TProtocolFactory protocolFactory,
                              ChannelHandlerContext ctx,
                              ByteBuf input,
                              ByteBuf output,
                              String msg,
                              int errorType) {
        ByteBuf errorOutput = output;

        boolean writtenSuccessfully = false;
        
        try {
            // 1. 에러 응답용 버퍼 준비 (전달받은 버퍼 재사용 또는 새로 생성)
            if (errorOutput == null) {
                errorOutput = ctx.alloc().buffer();
            }
            // output이 전달된 경우 clear()는 이미 호출된 상태
            
            // 2. 요청 메시지에서 sequence ID와 method name 읽기 (에러 응답에 필요)
            String methodName = "";
            int seqId = 0;
            
            // input 버퍼의 readerIndex를 저장하고 읽기 후 복원 (성능상 최적)
            int savedReaderIndex = input.readerIndex();
            try {
                TNettyChannelTransport readTransport = new TNettyChannelTransport(input, errorOutput);
                var protocol = protocolFactory.getProtocol(readTransport);
                
                var message = protocol.readMessageBegin();
                methodName = message.name;
                seqId = message.seqid;
            } catch (Exception readEx) {
                LOGGER.warn("Failed to read request message info for error response", readEx);
                methodName = "unknown";
                seqId = 0;
            } finally {
                // readerIndex 복원하여 이후 input 사용 시 처음부터 읽을 수 있도록 함
                input.readerIndex(savedReaderIndex);
            }
            
            // 3. 에러 응답 작성
            TNettyChannelTransport errorTransport = new TNettyChannelTransport(input, errorOutput);
            var errorProtocol = protocolFactory.getProtocol(errorTransport);
            
            // 에러 메시지 작성
            TApplicationException appException = new TApplicationException(errorType, msg);
            errorProtocol.writeMessageBegin(
                new org.apache.thrift.protocol.TMessage(
                    methodName,
                    org.apache.thrift.protocol.TMessageType.EXCEPTION,
                    seqId
                )
            );
            appException.write(errorProtocol);
            errorProtocol.writeMessageEnd();
            errorProtocol.getTransport().flush();
            
            // 4. 에러 응답 전송
            ctx.writeAndFlush(errorOutput);
            writtenSuccessfully = true;
            LOGGER.debug("Sending error response: {}", msg);
            
        } catch (Exception e) {
            LOGGER.error("Failed to create error response", e);
            
            // 연결 종료 (에러 응답조차 보낼 수 없는 상태인 경우가 대부분이므로 연결 끊고 재연결하도록 유도)
            ctx.close();
        } finally {
            if (!writtenSuccessfully && errorOutput != null && errorOutput.refCnt() > 0) {
                errorOutput.release();
            }
        }
    }

    private static int getErrorType(Throwable e) {
        if (e instanceof TApplicationException) {
            return ((TApplicationException) e).getType();
        } else if (e instanceof IllegalArgumentException) {
            return TApplicationException.INVALID_MESSAGE_TYPE;
        } else if (e instanceof UnsupportedOperationException) {
            return TApplicationException.UNKNOWN_METHOD;
        } else if (e instanceof SecurityException) {
            return TApplicationException.PROTOCOL_ERROR;
        } else {
            return TApplicationException.INTERNAL_ERROR;
        }
    }

    @Override
    public void run() {
        try {
            process(processor, protocolFactory, ctx, input, output);
        } catch (Throwable e) {
            LOGGER.error("Critical error in Thrift request processing", e);
            ctx.fireExceptionCaught(e);
        }
    }
}
