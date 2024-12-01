package com.binghe.client;

import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

public class TcpHelloWorldclient {

    public static void main(String[] args) {
        Connection connection = TcpClient.create()
                .host("127.0.0.1")
                .port(8080)
                .handle((in, out) -> {
                    in.receive()
                            .log("[Received Data]")
                            .ofType(String.class)
                            .subscribe(System.out::println);

                    return out
                            .sendString(Mono.just("hello world"));
                })
                .connectNow();

        connection.onDispose()
                .block();
    }
}
