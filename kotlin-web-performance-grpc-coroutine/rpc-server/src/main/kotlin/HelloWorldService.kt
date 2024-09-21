import com.binghe.proto.HelloRequest
import com.binghe.proto.HelloResponse
import com.binghe.proto.HelloServiceGrpcKt
import kotlinx.coroutines.delay

class HelloWorldService : HelloServiceGrpcKt.HelloServiceCoroutineImplBase() {

    override suspend fun hello(request: HelloRequest): HelloResponse {
        delay(1_000L)
        val response = "Hello ${request.firstName} ${request.lastName}"
        println("HelloWorldService.hello called. $response")
        return HelloResponse.newBuilder()
            .setGreeting(response)
            .build()
    }
}