package com.binghe.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class HelloServiceGrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new HelloServiceImpl())
                .build();

        server.start();
        server.awaitTermination();
    }
}
