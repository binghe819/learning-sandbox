package com.binghe.member

import com.binghe.MemberService
import com.binghe.proto.GetMemberRequest
import com.binghe.proto.GetMemberResponse
import com.binghe.proto.MemberGrpcServiceGrpcKt
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class MemberGrpcServiceImpl(private val memberService: MemberService) : MemberGrpcServiceGrpcKt.MemberGrpcServiceCoroutineImplBase() {

    override suspend fun getMember(request: GetMemberRequest): GetMemberResponse {
        val member = memberService.findById(request.id)
        return GetMemberResponse.newBuilder()
            .setId(member?.id!!)
            .setName(member.name)
            .setAge(member.age ?: 0)
            .setDescription(member.description)
            .build()
    }
}