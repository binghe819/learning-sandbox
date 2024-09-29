package com.binghe;

import com.binghe.proto.GetMemberRequest;
import com.binghe.proto.GetMemberResponse;
import com.binghe.proto.HelloRequest;
import com.binghe.proto.HelloResponse;
import com.binghe.proto.HelloServiceGrpc;
import com.binghe.proto.MemberGrpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

    private ManagedChannel channel;
    private HelloServiceGrpc.HelloServiceBlockingStub helloBlockingStub;
    private MemberGrpcServiceGrpc.MemberGrpcServiceBlockingStub memberGrpcServiceBlockingStub;

    public GrpcClientService() {
        this.channel = ManagedChannelBuilder.forAddress("127.0.0.1", 9090)
                .usePlaintext()
                .build();
        helloBlockingStub = HelloServiceGrpc.newBlockingStub(channel);
        memberGrpcServiceBlockingStub = MemberGrpcServiceGrpc.newBlockingStub(channel);
    }

    public String sendBlockingUnary(HelloRequest request) {
        HelloResponse helloResponse = helloBlockingStub.hello(request);
        return helloResponse.getGreeting();
    }

    public GetMemberResponse getMember(GetMemberRequest request) {
        return memberGrpcServiceBlockingStub.getMember(request);
    }
}