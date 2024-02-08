package com.binghe;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackPressureExample {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BackPressureExample.class);

    public static void main(String[] args) {
        Flux.range(1, 5)
                .doOnRequest(data -> log.info("# doInRequest: {}", data))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        sleep(3);
                        log.info("# hookOnNext: {}", value);
                        request(1);
                    }
                });
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
