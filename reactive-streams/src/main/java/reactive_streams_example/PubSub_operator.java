package reactive_streams_example;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Reactive Streams - Operator
 */
public class PubSub_operator {

    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(5).collect(toList()));

        // 1. publisher에 subscriber 구독 요청.
        pub.subscribe(logSub());
    }

    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                // 3. Publisher로부터 전달받은 Subscription (구독 정보)을 통해 데이터를 요청한다.
                System.out.println("Sub onSubscribe.");
                this.subscription = subscription;
                // Subscription을 통해 데이터 request
                this.subscription.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer item) {
                // 5. Publisher에서 전송한 데이터를 받아서 Subscriber에서 처리.
                System.out.println("Sub onNext. item : " + item);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Sub onError.");
            }

            @Override
            public void onComplete() {
                System.out.println("Sub onComplete");
            }
        };
    }

    private static Publisher<Integer> iterPub(Iterable<Integer> data) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                // 2. Publisher에서 Subscriber에게 Subscription (구독 정보)를 전달한다.
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        // 4. 요청에 따라 Subscriber에게 n개 만큼의 데이터를 전송.
                        // 여기선 우선 모든 데이터 전부 보내는 것으로 예시 작성함.
                        System.out.println("Pub request. n : " + n);
                        try {
                            data.forEach(it -> subscriber.onNext(it));
                            subscriber.onComplete();
                        } catch (Throwable e) {
                            subscriber.onError(e);
                        }
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
        };
    }
}
