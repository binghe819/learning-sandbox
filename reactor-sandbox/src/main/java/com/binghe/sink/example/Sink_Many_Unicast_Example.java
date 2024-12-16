package com.binghe.sink.example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Sink_Many_Unicast_Example {

    public static void main(String[] args) {
        Sinks.Many<String> sinks = Sinks.many().unicast().onBackpressureBuffer();

        Flux<String> flux = sinks.asFlux();

        flux.log().subscribe(data -> System.out.println("1번 구독자: " + data)); // 구독자 추가

        sinks.tryEmitNext("1");
        sinks.tryEmitNext("2");
        sinks.tryEmitNext("3");

        sinks.tryEmitComplete();
    }
}
