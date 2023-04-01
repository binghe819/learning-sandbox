package reactive_streams_scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

/**
 * 여러 스레드 에서 실행되는 Reactive Streams (비동기)
 *
 * TO-BE:  Scheduler를 이용해서 각 아이템을 비동기적으로 실행한다. -> subscribeOn 방식
 *
 *
 *
 */
public class PubSub_Scheduler_async_subscribeOn {

    private static Logger log = LoggerFactory.getLogger(PubSub_Scheduler_async_subscribeOn.class);

    public static void main(String[] args) {
        Flow.Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Flow.Subscription() {
                @Override
                public void request(long n) {
                    log.debug("request()");
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {
                }
            });
        };
        //pub

        // pub과 sub 사이에 다른 Thread에서 동작하도록 설정.
        Flow.Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor();

            es.execute(() -> pub.subscribe(sub));

            es.shutdown();
        };

        // sub
        subOnPub.subscribe(new Flow.Subscriber<Integer>() {
            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                log.debug("onSubscribe");
                this.subscription = subscription;
                subscription.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer item) {
                log.debug("onNext: {}", item);
            }

            @Override
            public void onError(Throwable throwable) {
                log.debug("onError: {}", throwable);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });

        log.debug("Exit");
    }
}
