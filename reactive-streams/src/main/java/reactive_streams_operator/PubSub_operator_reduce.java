package reactive_streams_operator;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Reactive Streams - Operator (reduce)
 * - reduce는 항상 초기 데이터가 존재하며, N번째 데이터와 N + 1번째 데이터를 특정 연산을하는 것을 반복해서 마지막 결과를 얻는 방법이다.
 */
public class PubSub_operator_reduce {

    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(5).collect(toList()));
        Publisher<Integer> reducePub = reducePub(pub, 0, (a, b) -> a + b);
        reducePub.subscribe(logSub());

        Publisher<String> stringReducePub = reducePub(pub, "", (a, b) -> a + " - " +  b);
        stringReducePub.subscribe(logSub());
    }

    // T -> R
    private static <T, R> Publisher<R> reducePub(Publisher<T> pub, R init, BiFunction<R, T, R> func) {
        return new Publisher<R>() {
            // logSub이 호출하는 subscribe
            @Override
            public void subscribe(Subscriber<? super R> subscriber) {
                System.out.println("mapPub subscribe");
                pub.subscribe(new Subscriber<T>() {
                    R result = init;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscriber.onSubscribe(subscription);
                    }

                    @Override
                    public void onNext(T item) {
                        // sum과 같은 역할.
                        result = func.apply(result, item);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
                        // 모든 데이터를 pub으로부터 받았을 때, 모든 더한 값을 Subscriber에게 전달한다.
                        subscriber.onNext(result);
                        subscriber.onComplete();
                    }
                });
            }
        };
    }

    private static <T> Subscriber<T> logSub() {
        return new Subscriber<T>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("Sub onSubscribe.");
                this.subscription = subscription;
                this.subscription.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(T item) {
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
