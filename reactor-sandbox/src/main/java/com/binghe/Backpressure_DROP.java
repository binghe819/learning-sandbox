package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Backpressure_DROP {

    private static final Logger log = LoggerFactory.getLogger(Backpressure_DROP.class);

    public static void main(String[] args) {
        Flux
            .interval(Duration.ofMillis(1L))
            .doOnNext(data -> log.info("# emitted by Flux: {}", data))
            .onBackpressureDrop(dropped -> log.info("# dropped: {}", dropped)) // DROP backpressure strategy
            .subscribeOn(Schedulers.boundedElastic())
            .publishOn(Schedulers.boundedElastic())
            .subscribe(data -> {
                sleep(500);
                log.info("# onNext: {}", data);
            },
            error -> log.error("# onError"));

//        publisher에서 emit할 데이터를 버퍼에 채움.
//        Flux<Object> fluxAsyncBackp = Flux.create(emitter -> {
//            // Publish 1000 numbers
//            for (int i = 0; i < 1000; i++) {
//                System.out.println(Thread.currentThread().getName() + " | Publishing = " + i);
//                // BackpressureStrategy.MISSING will cause MissingBackpressureException
//                // eventually
//                emitter.next(i);
//            }
//            // When all values or emitted, call complete.
//            emitter.complete();
//
//        }, FluxSink.OverflowStrategy.DROP);

        sleep(2000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
