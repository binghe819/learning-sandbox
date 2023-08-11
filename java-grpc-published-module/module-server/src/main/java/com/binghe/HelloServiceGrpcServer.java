package com.binghe;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class HelloServiceGrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(gRpcConfiguration.port)
                .addService(new HelloRemoteService())
                .build();

        server.start();
        server.awaitTermination();
    }
}
