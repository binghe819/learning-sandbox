package com.binghe;

import com.binghe.proto.GetMemberRequest;
import com.binghe.proto.GetMemberResponse;
import com.binghe.proto.HelloRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GrpcClientServiceTest {

    @Disabled
    @Test
    void hello_blocking_call() {
        GrpcClientService helloRemoteServiceCaller = new GrpcClientService();

        // when
        for (int i = 0; i < 10; i++) {
            HelloRequest request = HelloRequest.newBuilder().setFirstName("kim").setLastName("byeonghwa - " + i).build();
            helloRemoteServiceCaller.sendBlockingUnary(request);
        }
    }

//    @Disabled
    @Test
    void member_blocking_call() {
        // given
        GrpcClientService grpcClientService = new GrpcClientService();

        Long id = 2L;

        // when
        GetMemberRequest request = GetMemberRequest.newBuilder().setId(Math.toIntExact(id)).build();
        GetMemberResponse member = grpcClientService.getMember(request);

        // then
        System.out.println(member.getDescription());
    }
}