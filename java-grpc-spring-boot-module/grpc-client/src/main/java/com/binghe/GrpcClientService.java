package com.binghe;

import com.binghe.proto.HelloRequest;
import com.binghe.proto.HelloResponse;
import com.binghe.proto.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

    private ManagedChannel channel;
    private HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public GrpcClientService() {
        this.channel = ManagedChannelBuilder.forAddress("127.0.0.1", 9090)
                .usePlaintext()
                .build();
        blockingStub = HelloServiceGrpc.newBlockingStub(channel);
    }

    public String sendBlockingUnary(HelloRequest request) {
        HelloResponse helloResponse = blockingStub.hello(request);
        return helloResponse.getGreeting();
    }
}