package com.binghe;

import com.binghe.proto.HelloRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloRemoteServiceCallerTest {

    // gRPC 서버가 동작하고있어야 테스트가 통과됨.
//    @Disabled
    @DisplayName("gRPC Client 메시지 전송 테스트. Server가 local에 동작중에만 성공합니다.")
    @Test
    void sendMessageTest() throws InterruptedException {
        // given
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        HelloRemoteServiceCaller helloRemoteServiceCaller = new HelloRemoteServiceCaller(gRpcConfiguration.host, gRpcConfiguration.port);

        // when
        for (int i = 0; i < 10; i++) {
            final int idx = i;
            CompletableFuture.runAsync(() -> {
                HelloRequest request = HelloRequest.newBuilder().setFirstName("kim").setLastName("byeonghwa - " + idx).build();
                helloRemoteServiceCaller.sendFutureUnary(request);
            }, threadPool);

//            HelloRequest request = HelloRequest.newBuilder().setFirstName("kim").setLastName("byeonghwa - " + i).build();
//            helloRemoteServiceCaller.sendFutureUnary(request);
        }

        Thread.sleep(10 * 1000);

        // then
//        assertThat(result).isNotBlank();
//        System.out.printf(result);
    }
}