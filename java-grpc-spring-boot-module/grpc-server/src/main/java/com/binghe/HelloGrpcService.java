package com.binghe;

import com.binghe.proto.HelloRequest;
import com.binghe.proto.HelloResponse;
import com.binghe.proto.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloGrpcService extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = "Hello" + request.getFirstName() + "," + request.getLastName();
        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void helloServerStream(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        super.helloServerStream(request, responseObserver);
    }

    @Override
    public StreamObserver<HelloRequest> helloClientStream(StreamObserver<HelloResponse> responseObserver) {
        return super.helloClientStream(responseObserver);
    }

    @Override
    public StreamObserver<HelloRequest> helloBiStream(StreamObserver<HelloResponse> responseObserver) {
        return super.helloBiStream(responseObserver);
    }
}
