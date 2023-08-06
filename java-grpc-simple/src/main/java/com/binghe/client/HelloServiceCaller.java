package com.binghe.client;

import com.binghe.proto.HelloRequest;
import com.binghe.proto.HelloResponse;
import com.binghe.proto.HelloServiceGrpc;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelloServiceCaller {

    private ManagedChannel channel;
    private HelloServiceGrpc.HelloServiceBlockingStub blockingStub;
    private HelloServiceGrpc.HelloServiceStub asynStub;
    private HelloServiceGrpc.HelloServiceFutureStub futureStub;

    public HelloServiceCaller(String domain, int port) {
        channel = ManagedChannelBuilder.forAddress(domain, port)
                .usePlaintext()
                .build();
        blockingStub = HelloServiceGrpc.newBlockingStub(channel);
        asynStub = HelloServiceGrpc.newStub(channel);
        futureStub = HelloServiceGrpc.newFutureStub(channel);
    }

    public void sendBlockingUnary(HelloRequest request) {
        System.out.println("step1 : client 1 server 1 blocking");
        HelloResponse helloResponse = blockingStub.hello(request);
        System.out.println("step1 결과 : " + helloResponse.getGreeting());
        System.out.println("step1 끝");
    }

    public void sendAsynUnary(HelloRequest request) {
        System.out.println("step2 : client1 server 1 asyn");
        asynStub.hello(request, new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                System.out.println("step2 결과 : " + helloResponse.getGreeting());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("step2 통신 끝");
            }
        });
        System.out.println("step2 끝");
    }

    public void sendFutureUnary(HelloRequest request) {
        System.out.println("step3 : client 1 server 1 future");
        ListenableFuture<HelloResponse> future = futureStub.hello(request);
        HelloResponse response = null;
        try {
            response = future.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("step3 결과 : " + response.getGreeting());
        System.out.println("step3 끝");
    }

    public void sendBlockingServerStream(HelloRequest request) {
        System.out.println("step4 : client 1 server n blocking");
        Iterator<HelloResponse> responseIter = blockingStub.helloServerStream(request);
        responseIter.forEachRemaining(response -> {
            System.out.println("step4 결과 : " + response.getGreeting());
        });
        System.out.println("step4 끝");
    }

    public void sendAsynServerStream(HelloRequest request) {
        System.out.println("step5 : client 1 server n asyn");
        asynStub.helloServerStream(request, new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                System.out.println("step5 결과 : " + helloResponse.getGreeting());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("step5 통신 끝");
            }
        });
        System.out.println("step5 끝");
    }

    public void sendAsynClientStream(List<HelloRequest> requestList) {
        System.out.println("step6 : client n server 1 asyn");
        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                System.out.println("step6 결과 : " + helloResponse.getGreeting());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("step6 통신 끝");
            }
        };
        StreamObserver<HelloRequest> requestObserver = asynStub.helloClientStream(responseObserver);
        for (HelloRequest request : requestList) {
            requestObserver.onNext(request);
        }
        requestObserver.onCompleted();
        System.out.println("step6 끝");
    }

    public void sendAsynBiStream(List<HelloRequest> requestList) {
        System.out.println("step7 client n server n asny");
        StreamObserver<HelloResponse> responseObsever = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
                System.out.println("step7 결과 : " + helloResponse.getGreeting());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("step7 통신 끝");
            }
        };

        StreamObserver<HelloRequest> requestObsever = asynStub.helloBiStream(responseObsever);

        for (HelloRequest request : requestList) {
            requestObsever.onNext(request);
        }

        requestObsever.onCompleted();
        System.out.println("step7 끝");
    }
}
