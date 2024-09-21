fun main() {
    val address = "localhost"
    val port = 9090
    val server = HelloWorldServer(address, port)
    server.start()
    server.blockUntilShutdown()
}