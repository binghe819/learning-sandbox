package com.binghe;

import com.binghe.proto.HelloRequest;
import com.binghe.proto.HelloResponse;
import com.binghe.proto.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HelloRemoteService extends HelloServiceGrpc.HelloServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(HelloRemoteService.class);

    private static final String HELLO = "Hello ! ";

    // unary
    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

        String greeting = HELLO + request.getFirstName() + "," + request.getLastName();

        log.info("[server logging] {}", greeting);

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // server stream
    @Override
    public void helloServerStream(HelloRequest request,
                                  StreamObserver<HelloResponse> responseObserver) {

        List<String> greetingList = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            greetingList.add(HELLO + request.getFirstName() + "," + request.getLastName() + ":" + i);
        }

        for (String greeting : greetingList) {
            HelloResponse response = HelloResponse.newBuilder()
                    .setGreeting(greeting)
                    .build();

            responseObserver.onNext(response);
            log.info("[server logging] server stream - send to client. message: {}", response);
        }

        responseObserver.onCompleted();
    }

    // client stream
    @Override
    public StreamObserver<HelloRequest> helloClientStream(
            StreamObserver<HelloResponse> responseObserver) {
        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest helloRequest) {
                System.out.println(HELLO + helloRequest.getFirstName() + "," + helloRequest.getLastName());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("[server logging] error - {}", throwable.toString());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(HelloResponse.newBuilder().setGreeting("success").build());
                responseObserver.onCompleted();
            }
        };
    }

    // bi stream
    @Override
    public StreamObserver<HelloRequest> helloBiStream(
            StreamObserver<HelloResponse> responseObserver) {
        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest helloRequest) {
                String greeting = HELLO + helloRequest.getFirstName() + "," + helloRequest.getLastName();
                System.out.println(greeting);

                responseObserver.onNext(HelloResponse.newBuilder().setGreeting(greeting+"1").build());
                responseObserver.onNext(HelloResponse.newBuilder().setGreeting(greeting+"2").build());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("[server logging] error - {}", throwable.toString());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
