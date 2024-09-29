package com.binghe;

import com.binghe.proto.HelloRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GrpcClientServiceTest {

//    @Disabled
    @Test
    void blocking_call() {
        GrpcClientService helloRemoteServiceCaller = new GrpcClientService();

        // when
        for (int i = 0; i < 10; i++) {
            HelloRequest request = HelloRequest.newBuilder().setFirstName("kim").setLastName("byeonghwa - " + i).build();
            helloRemoteServiceCaller.sendBlockingUnary(request);
        }
    }
}