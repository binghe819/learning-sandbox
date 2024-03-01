package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotSequence {

    private static final Logger log = LoggerFactory.getLogger(HotSequence.class);

    public static void main(String[] args) throws InterruptedException {
        String[] singers = {"A", "B", "C", "D", "E"};

        log.info("# Begin convert: ");

        Flux<String> concertFlux = Flux.fromArray(singers)
                .delayElements(Duration.ofSeconds(1))
                .share();// Hot Sequence

        concertFlux.subscribe(singer -> log.info("# Subscriber1 is watching {}'s song", singer));

        Thread.sleep(2000);

        concertFlux.subscribe(singer -> log.info("# Subscriber2 is watching {}'s song", singer));

        Thread.sleep(3000);
    }
}
