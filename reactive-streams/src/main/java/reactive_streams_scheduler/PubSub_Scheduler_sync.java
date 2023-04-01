package reactive_streams_scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;

/**
 * 하나의 스레드 (main 스레드)에서 실행되는 Reactive Streams
 *
 * AS-IS: 현재 아래 코드는 각각의 item이 Sequential하게 동작한다. 즉, 동기적으로 동작하는 것.
 *        해당 스레드는 계속해서 Blocking된다. 이럼 사실 Push 방식을 사용할 이유가 없다.
 *        이벤트가 발생했을 때 비동기적으로 해당 이벤트를 처리할 필요가 있음.
 *
 * 실제 Reactive Streams로 코드를 작성할 땐, PubSub 모두를 하나의 스레드에서 동작하도록하는 이런 직렬적인 방식을 사용하지 않는다.
 *
 * 서로 다른 스레드에서 비동기로 동작시키는 경우가 대부분이다.
 */
public class PubSub_Scheduler_sync {

    private static Logger log = LoggerFactory.getLogger(PubSub_Scheduler_sync.class);

    public static void main(String[] args) {
        Flow.Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Flow.Subscription() {
                @Override
                public void request(long n) {
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

        pub.subscribe(new Flow.Subscriber<Integer>() {
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
