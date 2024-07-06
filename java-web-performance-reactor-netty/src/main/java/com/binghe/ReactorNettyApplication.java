package com.binghe;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class ReactorNettyApplication {
    public static void main(String[] args) {
        DisposableServer server =
                HttpServer.create()
                        .host("localhost")
                        .port(8080)
                        .route(routes ->
                        routes.get("/hello",
                                        (request, response) -> response.sendString(Mono.just("Hello World!"))))
                        .bindNow();

        server.onDispose()
                .block();
    }
}