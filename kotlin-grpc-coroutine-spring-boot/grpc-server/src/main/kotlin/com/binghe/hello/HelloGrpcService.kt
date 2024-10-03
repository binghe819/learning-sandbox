package com.binghe.com.binghe.hello


import com.binghe.proto.HelloRequest
import com.binghe.proto.HelloResponse
import com.binghe.proto.HelloServiceGrpcKt.HelloServiceCoroutineImplBase
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class HelloGrpcService : HelloServiceCoroutineImplBase() {

    override suspend fun hello(request: HelloRequest): HelloResponse {
        val response = "Hello ${request.firstName} ${request.lastName}"
        println("HelloWorldService.hello called. $response")
        return HelloResponse.newBuilder()
            .setGreeting(response)
            .build()
    }
}