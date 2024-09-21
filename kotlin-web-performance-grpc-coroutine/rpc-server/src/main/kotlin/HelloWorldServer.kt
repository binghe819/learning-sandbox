import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.grpc.Server
import io.grpc.ServerBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HelloWorldServer(private val address: String, private val port: Int) {

    val threadPool: ExecutorService = Executors.newFixedThreadPool(100, ThreadFactoryBuilder().setNameFormat("rpc-server-executor-%d").build())

    val server: Server =
        ServerBuilder
            .forPort(port)
            .addService(HelloWorldService())
            .executor(threadPool)
            .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@HelloWorldServer.stop()
                println("*** server shut down")
            },
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}