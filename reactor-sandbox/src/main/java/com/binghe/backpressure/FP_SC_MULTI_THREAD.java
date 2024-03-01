package com.binghe.backpressure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FP_SC_MULTI_THREAD {

    private static final Logger log = LoggerFactory.getLogger(FP_SC_MULTI_THREAD.class);

    public static void main(String[] args) {
        produce(0, 500)
                .doOnRequest(request -> log.info("# request: {}", request))
                .subscribeOn(Schedulers.parallel())
                .publishOn(Schedulers.parallel())
                .flatMap(value -> Mono.fromSupplier(() -> {
                    sleep(100L);
                    log.info("Consumed {}", value); // onNext()
                    return null;
                }).subscribeOn(Schedulers.parallel()), 10)
                .subscribe();

        sleep(100000L);
    }

    private static Flux<Long> produce(long delayBetweenEmits, long upto) {
        return Flux.generate(
                () -> 1L,
                (state, sink) -> {
                    sleep(delayBetweenEmits);
                    long nextState = state + 1;
                    if (state > upto) {
                        sink.complete();
                        return nextState;
                    } else {
                        log.info("Emitted {}", state);
                        sink.next(state);
                        return nextState;
                    }
                }
        );
    }

    private static void sleep(Long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
