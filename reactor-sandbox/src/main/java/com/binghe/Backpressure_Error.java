package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Backpressure_Error {

    private static final Logger log = LoggerFactory.getLogger(Backpressure_Error.class);

    public static void main(String[] args) {
        Flux<Long> flux = Flux
                .interval(Duration.ofMillis(1L))
                .doOnRequest(data -> log.info("# requested: {}", data))
                .doOnNext(data -> log.info("# emitted by Flux: {}", data));
        Flux<Long> onBackpressureError = flux.onBackpressureError();
        Flux<Long> publishOn = onBackpressureError.publishOn(Schedulers.parallel());
        Disposable subscriber = publishOn.subscribe(data -> {
            sleep(5);
            log.info("# onNext: {}", data);
        });

        sleep(100000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
