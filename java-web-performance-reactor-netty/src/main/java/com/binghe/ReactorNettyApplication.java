package com.binghe;

import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class ReactorNettyApplication {

    public static void main(String[] args) {
        MemberService memberService = new MemberService();

        DisposableServer server =
                HttpServer.create()
                        .host("localhost")
                        .port(8080)
                        .route(routes ->
                            routes.get("/member/short", (request, response) -> response.sendString(memberService.findById(0L)))
                                  .get("/member/long", (request, response) -> response.sendString(memberService.findByIdLong(0L)))
                        )
                        .bindNow();

        server.onDispose()
                .block();
    }
}