package com.binghe;

import reactor.core.publisher.Flux;

public class FluxHelloWorld {

    public static void main(String[] args) {
        Flux<String> sequence = Flux.just("Hello", "Flux"); // Data Source

        sequence
                .map(String::toLowerCase)
                .subscribe(System.out::println);
    }
}
