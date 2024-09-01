package com.binghe;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloServiceGrpcServer {

    private static final Logger log = LoggerFactory.getLogger(HelloServiceGrpcServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(gRpcConfiguration.port)
                .addService(new HelloRemoteService())
                .build();

        server.start();
        log.info("Server Started. port: {}", gRpcConfiguration.port);
        server.awaitTermination();
    }
}
