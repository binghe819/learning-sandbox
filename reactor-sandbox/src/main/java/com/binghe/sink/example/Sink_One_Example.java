package com.binghe.sink.example;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * 1 message : n subscribers
 */
public class Sink_One_Example {

    public static void main(String[] args) {
        Sinks.One<String> sink = Sinks.one();
        Mono<String> mono = sink.asMono();

        mono.log().subscribe(data -> System.out.println("1번 구독자: " + data)); // 구독자 추가
        mono.log().subscribe(data -> System.out.println("2번 구독자: " + data)); // 구독자 추가

        sink.tryEmitValue("1");
        sink.tryEmitValue("2");
    }
}
