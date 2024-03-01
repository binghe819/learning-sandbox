package com.binghe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Backpressure_LATEST {

    private static final Logger log = LoggerFactory.getLogger(Backpressure_LATEST.class);

    public static void main(String[] args) {
        Flux
            .interval(Duration.ofMillis(1L))
            .onBackpressureLatest() // LATEST backpressure strategy
            .publishOn(Schedulers.parallel()) // 이 메서드 다음 downstream부터 다른 스레드에서 실행.
            .subscribe(data -> { // subscriber에 publisher가 emit한 데이터가 전달된 경우 콜백 정의.
                sleep(5);
                log.info("# onNext: {}", data);
            },
            error -> log.error("# onError"));

        sleep(2000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
