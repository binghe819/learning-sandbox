package reactive_streams_operator;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Reactive Streams - Operator (reduce의 한 예시인 sum)
 */
public class PubSub_operator_sum {

    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(5).collect(toList()));
        Publisher<Integer> sumPub = sumPub(pub);
        sumPub.subscribe(logSub());
    }

    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {
        return new Publisher<Integer>() {
            // logSub이 호출하는 subscribe
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                System.out.println("mapPub subscribe");
                pub.subscribe(new Subscriber<Integer>() {
                    int sum = 0;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscriber.onSubscribe(subscription);
                    }

                    @Override
                    public void onNext(Integer item) {
                        // sum과 같은 역할.
                        sum += item;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
                        // 모든 데이터를 pub으로부터 받았을 때, 모든 더한 값을 Subscriber에게 전달한다.
                        subscriber.onNext(sum);
                        subscriber.onComplete();
                    }
                });
            }
        };
    }

    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("Sub onSubscribe.");
                this.subscription = subscription;
                this.subscription.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer item) {
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
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
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
