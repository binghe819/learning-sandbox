package com.binghe;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Netty Boss Group : 새로운 커넥션에 대한 처리를 담당하는 스레드그룹 (소량의 스레드)
 * Netty Worker Group : Network I/O를 주로 처리하는 스레드그룹 (비즈니스 처리 완료후 Worker Group에 처리가 완료되었다고 알린다)
 * executor : 요청에 대한 비즈니스 처리에 사용되는 스레드그룹
 */
public class HelloServiceGrpcServer {

    private static final Logger log = LoggerFactory.getLogger(HelloServiceGrpcServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        // Network는 Netty의 worker thread가 여전히 사용되지만, 비즈니스 처리는 executor 스레드로 처리한다.
        ExecutorService executorThreadPool = Executors.newFixedThreadPool(100, new ThreadFactoryBuilder().setNameFormat("grpc-request-executor-%d").build());

        // NettyServerBuilder를 사용하면 Boss, Child EventGroup 설정도 가능하다.
        Server server = ServerBuilder
                .forPort(gRpcConfiguration.port)
                .addService(new HelloRemoteService())
                .executor(executorThreadPool) //  it determines how the gRPC server processes the incoming requests. (서버 비즈니스 로직 처리 스레드)
//                .directExecutor() // This method tells gRPC to execute the RPC calls directly on the Netty I/O threads (or whichever threads are handling the network requests). This means that no new threads are created for executing the RPC calls—everything happens in the same thread that processes the network events.
                .build();

        server.start();
        log.info("Server Started. port: {}", gRpcConfiguration.port);
        server.awaitTermination();
    }
}
