package com.binghe.backpressure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * [First Ex] Fast Producer, Slow Consumer without Threading
 * ->
 */
public class FP_SC_WITHOUT_THREAD {

    private static final Logger log = LoggerFactory.getLogger(FP_SC_WITHOUT_THREAD.class);

    public static void main(String[] args) {
        produce(1000L, 100)
                .doOnRequest(request -> log.info("# request: {}", request))
                .subscribe(data -> {
                    log.info("Consumed {}", data); // onNext()
                });
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
