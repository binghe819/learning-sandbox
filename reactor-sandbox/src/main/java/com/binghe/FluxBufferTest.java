package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class FluxBufferTest {

    private static final Logger log = LoggerFactory.getLogger(FluxBufferTest.class);

    public static void main(String[] args) {
        Flux.range(1, 1000)
            .buffer(3)
            .doOnNext(data -> log.info("# emitted by Flux: {}", data))
            .onBackpressureDrop(dropped -> log.info("# dropped: {}", dropped)) // DROP backpressure strategy
            .publishOn(Schedulers.parallel())
            .subscribe(data -> {
                sleep(100);
                log.info("# onNext: {}", data);
            }, error -> log.error("# onError"));

        sleep(1000000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
