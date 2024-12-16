package com.binghe.sink.processor;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 일반 구독 (Flux-subscribe)
 *
 * 구독 이후에 발생한 데이터의 변화는 감지하지 않는다. 데이터가 정해져있고 거기에 구독자를 추가한다.
 */
public class one_flux_subscribe {

    public static void main(String[] args) {
        List<Integer> source = new ArrayList<>();

        source.addAll(List.of(1, 2, 3, 4, 5));

        Flux.fromIterable(source)
                .collectList()
                .log()
                .subscribe(System.out::println);

        Flux.fromIterable(source)
                .collectList()
                .log()
                .subscribe(System.out::println);

        // Publisher 데이터 소스에 데이터가 추가되어도 subscriber에게 전송하지 못한다. 매번 데이터는 정해져있고 거기에 구독자를 추가해야한다.
        source.addAll(List.of(6, 7, 8, 9));
    }
}
