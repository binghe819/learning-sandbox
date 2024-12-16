package com.binghe.sink.example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Sink_Many_Multicast_Replay_Example {

    public static void main(String[] args) {
        Sinks.Many<String> sinks = Sinks.many().replay().all();

        sinks.tryEmitNext("1"); // 구독자 없을 때 방출 한번.

        Flux<String> flux = sinks.asFlux();
        // 구독자 추가
        flux.log().subscribe(data -> System.out.println("1번 구독자: " + data));

        // 구독자 추가후 방출 두 번.
        sinks.tryEmitNext("2");
        sinks.tryEmitNext("3");

        // 구독자 추가
        flux.log().subscribe(data -> System.out.println("2번 구독자: " + data));

        // 구독자 추가후 방출 두 번.
        sinks.tryEmitNext("4");
        sinks.tryEmitNext("5");
    }
}
