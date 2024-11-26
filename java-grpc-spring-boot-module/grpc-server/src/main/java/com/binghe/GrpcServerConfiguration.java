package com.binghe;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.Codec;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcServerConfiguration {

    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(200, new ThreadFactoryBuilder().setNameFormat("rpc-server-executor-%d").build());
    }

    @Bean
    public GrpcServerConfigurer keepAliveServerConfigurer() {
        return serverBuilder -> {
            CompressorRegistry compressorRegistry = CompressorRegistry.newEmptyInstance();
            compressorRegistry.register(new Codec.Gzip());

            DecompressorRegistry decompressorRegistry = DecompressorRegistry.emptyInstance();
            decompressorRegistry.with(new Codec.Gzip(), true);

//            new CompressorRegistry()

            if (serverBuilder instanceof NettyServerBuilder) {
                ((NettyServerBuilder) serverBuilder)
                        .keepAliveTime(180, TimeUnit.SECONDS)
                        .keepAliveTimeout(5, TimeUnit.SECONDS)
                        .compressorRegistry(compressorRegistry)
                        .decompressorRegistry(decompressorRegistry)
                        .intercept(new ServerInterceptor() {
                            @Override
                            public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
                                serverCall.setCompression("gzip");
                                return serverCallHandler.startCall(serverCall, metadata);
                            }
                        })
//                        .executor(threadPool())
                        .permitKeepAliveWithoutCalls(true);
            }
        };
    }
}
