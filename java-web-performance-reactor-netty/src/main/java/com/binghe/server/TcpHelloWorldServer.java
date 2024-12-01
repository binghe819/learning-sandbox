package com.binghe.server;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

public class TcpHelloWorldServer {

    public static void main(String[] args) {
        DisposableServer server = TcpServer.create()
                .host("127.0.0.1")
                .port(8080)
                .handle((inbound, outbound) ->
                    inbound
                            .receive()
                            .asString()
                            .map(String::toUpperCase)
                            .flatMap(response ->
                                    outbound.sendString(Mono.just(response))
                            )
                )
                .bindNow();

        server.onDispose()
                .block();
    }
}
