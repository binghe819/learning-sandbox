package com.binghe.member;

import com.binghe.proto.GetMemberRequest;
import com.binghe.proto.GetMemberResponse;
import com.binghe.proto.MemberGrpcServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
public class MemberGrpcServiceImpl extends MemberGrpcServiceGrpc.MemberGrpcServiceImplBase {

    private final MemberService memberService;

    @Override
    public void getMember(GetMemberRequest request, StreamObserver<GetMemberResponse> responseObserver) {
        Long id = (long) request.getId();
        Member member = memberService.findById(id);
        GetMemberResponse response = GetMemberResponse.newBuilder()
                .setId(Math.toIntExact(member.getId()))
                .setName(member.getName())
                .setAge(member.getAge())
                .setDescription(member.getDescription())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMemberServerStream(GetMemberRequest request, StreamObserver<GetMemberResponse> responseObserver) {
        super.getMemberServerStream(request, responseObserver);
    }

    @Override
    public StreamObserver<GetMemberRequest> getMemberClientStream(StreamObserver<GetMemberResponse> responseObserver) {
        return super.getMemberClientStream(responseObserver);
    }

    @Override
    public StreamObserver<GetMemberRequest> getMemeberBiStream(StreamObserver<GetMemberResponse> responseObserver) {
        return super.getMemeberBiStream(responseObserver);
    }
}
