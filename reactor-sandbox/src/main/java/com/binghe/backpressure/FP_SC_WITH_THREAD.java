package com.binghe.backpressure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FP_SC_WITH_THREAD {

    private static final Logger log = LoggerFactory.getLogger(FP_SC_WITH_THREAD.class);

    public static void main(String[] args) {
        produce(0, 500)
                .doOnRequest(request -> log.info("# request: {}", request))
//                .onBackpressureDrop(dropped -> log.info("# dropped: {}", dropped))
                .subscribeOn(Schedulers.parallel())
//                .publishOn(Schedulers.parallel())
                .parallel(5)
                .runOn(Schedulers.parallel())
//                .publishOn(Schedulers.parallel(), 10) // prefetch 적용
                .subscribe(data -> {
                    sleep(100L);
                    log.info("Consumed {}", data); // onNext()
                });


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
