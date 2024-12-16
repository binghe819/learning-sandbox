package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ReactiveStreamsHelloWorld {

    private static final Logger log = LoggerFactory.getLogger(ReactiveStreamsHelloWorld.class);

    public static void main(String[] args) {
        Flux.range(0, 10)
                .log()
                .subscribe();
    }
}
