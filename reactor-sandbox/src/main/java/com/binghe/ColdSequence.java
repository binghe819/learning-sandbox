package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Arrays;

public class ColdSequence {

    private static final Logger log = LoggerFactory.getLogger(ColdSequence.class);

    public static void main(String[] args) throws InterruptedException {
        Flux<String> coldFlux = Flux
                .fromIterable(Arrays.asList("KOREA", "USA", "CANADA", "FRANCE", "JAPAN"))
                .map(String::toLowerCase);

        coldFlux.subscribe(country -> log.info("# Subscriber1: {}", country));
        System.out.println("===================================");

        Thread.sleep(2000);
        coldFlux.subscribe(country -> log.info("# Subscriber2: {}", country));
    }
}
