package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * 기본적으로 따로 설정하지 않는한 첫 request는 (256)을 보낸다.
 * 당연히 publisher는 request받은 256만큼 emit (onNext)를 호출한다.
 *
 */
public class Backpressure_Default_Buffer_Size {

    private static final Logger log = LoggerFactory.getLogger(Backpressure_Default_Buffer_Size.class);

    public static void main(String[] args) {
        Flux.range(1, 1000)
                .doOnRequest(request -> log.info("# request: {}", request))
                .doOnNext(data -> log.info("# emitted by Flux: {}", data)) // publisher가 onNext 호출.
                .onBackpressureError()
                .subscribeOn(Schedulers.parallel())
                .publishOn(Schedulers.parallel())
                .subscribe(data -> {
                    sleep(5);
                    log.info("# onNext: {}", data);
                }, error -> log.error("# onError: {}", error));

        sleep(100000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
