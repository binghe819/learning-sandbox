import com.binghe.proto.HelloRequest
import com.binghe.proto.HelloResponse
import com.binghe.proto.HelloServiceGrpcKt
import io.grpc.ManagedChannel
import java.io.Closeable
import java.util.concurrent.TimeUnit

class HelloServiceClient(private val channel: ManagedChannel) : Closeable {

    private val stub: HelloServiceGrpcKt.HelloServiceCoroutineStub = HelloServiceGrpcKt.HelloServiceCoroutineStub(channel)

    suspend fun hello(helloRequest: HelloRequest): HelloResponse {
        val response = stub.hello(helloRequest)
        return response
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}