package com.binghe.server;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class HttpHelloWorldServer {

    public static void main(String[] args) {
        DisposableServer server =
                HttpServer.create()
                        .host("localhost")
                        .port(8080)
                        .route(
                                routes -> routes.get("/helloworld", (request, response) -> response.sendString(Mono.just("Hello World Get")))
                                                .post("helloworld", (request, response) -> response.sendString(Mono.just("Hello World Post")))
                        )
                        .bindNow();

        server.onDispose()
                .block();
    }
}
