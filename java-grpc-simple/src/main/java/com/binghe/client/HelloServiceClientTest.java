package com.binghe.client;

import com.binghe.proto.HelloRequest;

import java.util.List;

public class HelloServiceClientTest {

    public static void main(String[] args) throws InterruptedException {
        // channel, stub
        HelloServiceCaller caller = new HelloServiceCaller("localhost", 9090);

        // requests
        HelloRequest A = HelloRequest.newBuilder().setFirstName("A").setLastName("AA").build();;
        HelloRequest B = HelloRequest.newBuilder().setFirstName("b").setLastName("bb").build();;
        HelloRequest C = HelloRequest.newBuilder().setFirstName("c").setLastName("cc").build();

        caller.sendBlockingUnary(A);
        caller.sendAsynUnary(A);
        caller.sendFutureUnary(A);
        caller.sendBlockingServerStream(A);
        caller.sendAsynServerStream(A);
        caller.sendAsynClientStream(List.of(A, B, C));
        caller.sendAsynBiStream(List.of(A, B, C));

        Thread.sleep(10000);
    }
}
