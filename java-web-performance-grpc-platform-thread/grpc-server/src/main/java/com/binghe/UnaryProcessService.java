package com.binghe;

import com.binghe.proto.HelloRequest;
import com.binghe.proto.HelloResponse;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnaryProcessService {

    private static final Logger log = LoggerFactory.getLogger(UnaryProcessService.class);
    private static final String HELLO = "Hello ! ";
    private final ExecutorService threadPool = Executors.newFixedThreadPool(100, new ThreadFactoryBuilder().setNameFormat("process-thread-%d").build());

    // unary Synchronous (Blocking) Processing (Netty의 Worker Thread가 blocking되진 않는다.)
    // 동기 + 블로킹 처리
    public void helloUnarySync(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = HELLO + request.getFirstName() + "," + request.getLastName();

//        sleep(3000);

        log.info("[server logging] {}", greeting);

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // unary Asynchronous (Non-blocking) Processing
    public void helloUnaryAsync(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
