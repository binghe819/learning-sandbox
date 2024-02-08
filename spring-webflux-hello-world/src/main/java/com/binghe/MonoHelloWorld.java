package com.binghe;

import reactor.core.publisher.Mono;

public class MonoHelloWorld {

    public static void main(String[] args) {
        Mono.just("Hello Mono")
                .subscribe(data -> System.out.println(data));
    }
}
