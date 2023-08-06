package com.binghe.client;

import com.binghe.proto.HelloRequest;

import java.util.ArrayList;
import java.util.List;

public class HelloServiceClient {

    public static void main(String[] args) {
        // channel, stub 준비
        HelloServiceCaller caller = new HelloServiceCaller("localhost", 9090);

        // Request들 준비
        List<HelloRequest> requestList = new ArrayList<>();
        requestList.add(HelloRequest.newBuilder().setFirstName("a").setLastName("aa").build());
        requestList.add(HelloRequest.newBuilder().setFirstName("b").setLastName("bb").build());
        requestList.add(HelloRequest.newBuilder().setFirstName("c").setLastName("cc").build());

        caller.sendBlockingUnary(requestList.get(0));
        caller.sendAsynUnary(requestList.get(0));
        caller.sendFutureUnary(requestList.get(0));
        caller.sendBlockingServerStream(requestList.get(0));
        caller.sendAsynServerStream(requestList.get(0));
        caller.sendAsynClientStream(requestList);
        caller.sendAsynBiStream(requestList);
    }
}
