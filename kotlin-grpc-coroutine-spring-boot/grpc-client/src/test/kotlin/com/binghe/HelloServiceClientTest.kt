package com.binghe

import com.binghe.proto.HelloRequest
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class HelloServiceClientTest {

    @Test
    fun sendMessageTest() {
        // given
        val address = "localhost"
        val port = 9090

        val channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()

        val client = HelloServiceClient(channel)

        val request = HelloRequest.newBuilder()
            .setFirstName("kim")
            .setLastName("byeong hwa")
            .build()

        // when
        runBlocking {

            for (i in 1..100) {
                val hello = client.hello(request)
            }
            delay(100_000)
        }
    }
}