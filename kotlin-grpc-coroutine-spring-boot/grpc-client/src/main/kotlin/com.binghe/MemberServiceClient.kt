package com.binghe

import com.binghe.proto.GetMemberRequest
import com.binghe.proto.GetMemberResponse
import com.binghe.proto.MemberGrpcServiceGrpcKt
import io.grpc.ManagedChannel
import java.io.Closeable
import java.util.concurrent.TimeUnit

class MemberServiceClient(private val channel: ManagedChannel) : Closeable {

    private val stub: MemberGrpcServiceGrpcKt.MemberGrpcServiceCoroutineStub =
        MemberGrpcServiceGrpcKt.MemberGrpcServiceCoroutineStub(channel)

    suspend fun getMember(getMemberRequest: GetMemberRequest) : GetMemberResponse {
        val member = stub.getMember(getMemberRequest)
        return member
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}