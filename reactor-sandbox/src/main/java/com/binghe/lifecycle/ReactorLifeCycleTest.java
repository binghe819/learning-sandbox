package com.binghe.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public class ReactorLifeCycleTest {

    private static final Logger log = LoggerFactory.getLogger(ReactorLifeCycleTest.class);

    public static void main(String[] args) {
        Flux.range(1, 10)
                .doOnNext(data -> log.info("range -> filter onNext: {}", data))
                .filter(data -> data % 2 == 0)
                .doOnNext(data -> log.info("filter -> map onNext: {}", data))
                .map(data -> data * 2)
                .doOnNext(data -> log.info("map -> subscriber onNext: {}", data))
                .subscribe(data -> log.info("# onNext: {}", data));


//        Flux<Integer> fluxRange = Flux.range(1, 10);
//        Flux<Integer> filteredFlux = fluxRange.filter(data -> data % 2 == 0);
//        Flux<Integer> mappedFlux = filteredFlux.map(data -> data * 2);
//        mappedFlux.concatWith()
//        Disposable subscribe = mappedFlux.subscribe(data -> log.info("# onNext: {}", data));
    }
}
