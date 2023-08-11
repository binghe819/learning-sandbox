package com.binghe;

import com.binghe.proto.HelloRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloRemoteServiceCallerTest {

    @DisplayName("gRPC Client 메시지 전송 테스트. Server가 local에 동작중에만 성공합니다.")
    @Test
    void sendMessageTest() {
        // given
        HelloRemoteServiceCaller helloRemoteServiceCaller = new HelloRemoteServiceCaller(gRpcConfiguration.host, gRpcConfiguration.port);

        HelloRequest request = HelloRequest.newBuilder().setFirstName("kim").setLastName("byeonghwa").build();

        // when
        String result = helloRemoteServiceCaller.sendBlockingUnary(request);

        // then
        assertThat(result).isNotBlank();
        System.out.printf(result);
    }
}