package com.binghe;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class TestExample_2 {

    private static final Logger log = LoggerFactory.getLogger(TestExample_2.class);

    public static void main(String[] args) {
        Flux.range(1, 10)
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer % 2 == 0) {
                            int data = integer * 2;
                            log.info("# data: {}", data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });
    }
}
