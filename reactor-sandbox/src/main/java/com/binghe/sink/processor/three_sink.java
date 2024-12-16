package com.binghe.sink.processor;

import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor deprecated로 인해 sink로 전환.
 */
public class three_sink {

    public static void main(String[] args) {
        List<String> source = new ArrayList<>();

        source.addAll(List.of("1", "2", "3", "4", "5"));

        Sinks.Many<List<String>> sink = Sinks.many().multicast().directBestEffort(); // 발행자

        sink.asFlux().log().subscribe(data -> System.out.println("1번 구독자: " + data)); // 구독자 추가
        sink.asFlux().log().subscribe(data -> System.out.println("2번 구독자: " + data)); // 구독자 추가
        sink.tryEmitNext(source); // 데이터 발행.

        System.out.println("\n--- Publisher는 Subscriber의 동의없이도 추가된 데이터를 emit 한다 ---\n");

        source.addAll(List.of("6", "7", "8")); // 데이// 터 추가
        sink.tryEmitNext(source); // 데이터 발행.
        sink.tryEmitComplete();
    }
}
