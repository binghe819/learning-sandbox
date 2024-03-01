package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestExample {

    private static final Logger log = LoggerFactory.getLogger(TestExample.class);

    public static void main(String[] args) {
//        Flux.range(1, 10)
//                .filter(i -> i % 2 == 0)
//                .map(i -> i * 2)
//                .subscribe(data -> log.info("# data: {}", data));

        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(elements::add);
    }
}
