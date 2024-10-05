package com.binghe;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
            if (serverBuilder instanceof NettyServerBuilder) {
                ((NettyServerBuilder) serverBuilder)
                        .keepAliveTime(30, TimeUnit.SECONDS)
                        .keepAliveTimeout(5, TimeUnit.SECONDS)
//                        .executor(threadPool())
                        .permitKeepAliveWithoutCalls(true);
            }
        };
    }
}
