package com.binghe

import com.binghe.proto.GetMemberRequest
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MemberServiceClientTest {

    @Test
    fun getMember_test() {
        // given
        val id = 1L
        val address = "localhost"
        val port = 9090

        val channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()

        val client = MemberServiceClient(channel)

        val request = GetMemberRequest.newBuilder()
            .setId(1L)
            .build()

        // when
        runBlocking {
            val member = client.getMember(request)
            println(member)
            delay(10_000)
        }
    }
}
