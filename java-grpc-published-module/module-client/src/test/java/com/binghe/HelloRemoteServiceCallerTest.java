package com.binghe;

import com.binghe.proto.HelloRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloRemoteServiceCallerTest {

    // gRPC 서버가 동작하고있어야 테스트가 통과됨.
//    @Disabled
    @DisplayName("gRPC Client 메시지 전송 테스트. Server가 local에 동작중에만 성공합니다.")
    @Test
    void sendMessageTest() {
        // given
        HelloRemoteServiceCaller helloRemoteServiceCaller = new HelloRemoteServiceCaller(gRpcConfiguration.host, gRpcConfiguration.port);

        // when
        for (int i = 0; i < 10; i++) {
            HelloRequest request = HelloRequest.newBuilder().setFirstName("kim").setLastName("byeonghwa - " + i).build();
            helloRemoteServiceCaller.sendFutureUnary(request);
        }

        // then
//        assertThat(result).isNotBlank();
//        System.out.printf(result);
    }
}