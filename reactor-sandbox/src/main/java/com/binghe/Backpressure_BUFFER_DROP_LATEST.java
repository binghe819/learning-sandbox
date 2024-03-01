package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Backpressure_BUFFER_DROP_LATEST {

    private static final Logger log = LoggerFactory.getLogger(Backpressure_BUFFER_DROP_LATEST.class);

    public static void main(String[] args) {
        Flux
            .interval(Duration.ofMillis(1L))
            .doOnNext(data -> log.info("# emitted by original Flux: {}", data)) // publisher가 emit한 데이터 로그 확인용
            .onBackpressureBuffer(2, // 버퍼 크기
                dropped -> log.info("# Overflow & Dropped: {}", dropped),
                BufferOverflowStrategy.DROP_LATEST
            )
            .publishOn(Schedulers.parallel()) // 이 메서드 다음 downstream부터 다른 스레드에서 실행.
            .subscribe(data -> {
                sleep(1000);
                log.info("# onNext: {}", data);
            }, error -> log.error("# onError"));

        sleep(3000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
