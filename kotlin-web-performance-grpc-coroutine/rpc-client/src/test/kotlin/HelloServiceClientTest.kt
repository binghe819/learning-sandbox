import com.binghe.proto.HelloRequest
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

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
            val hello = client.hello(request)
            println(hello)
        }
    }
}

//    // gRPC 서버가 동작하고있어야 테스트가 통과됨.
////    @Disabled
//    @DisplayName("gRPC Client 메시지 전송 테스트. Server가 local에 동작중에만 성공합니다.")
//    @Test
//    void sendMessageTest() {
//        // given
//        HelloRemoteServiceCaller helloRemoteServiceCaller = new HelloRemoteServiceCaller(gRpcConfiguration.host, gRpcConfiguration.port);
//
//        // when
//        for (int i = 0; i < 10; i++) {
//            HelloRequest request = HelloRequest.newBuilder().setFirstName("kim").setLastName("byeonghwa - " + i).build();
//            helloRemoteServiceCaller.sendFutureUnary(request);
//        }
//
//        // then
////        assertThat(result).isNotBlank();
////        System.out.printf(result);
//    }