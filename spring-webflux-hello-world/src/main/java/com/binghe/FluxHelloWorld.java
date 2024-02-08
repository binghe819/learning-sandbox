package com.binghe;

import reactor.core.publisher.Flux;

public class FluxHelloWorld {

    public static void main(String[] args) {
        Flux<String> sequence = Flux.just("Hello", "Reactor"); // Data Source

        sequence
                .map(data -> data.toLowerCase())
                .subscribe(data -> System.out.println(data));
    }
}
