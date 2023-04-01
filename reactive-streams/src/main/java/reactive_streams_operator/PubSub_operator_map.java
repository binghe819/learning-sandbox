package reactive_streams_operator;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Reactive Streams - Operator (map)
 *
 * 제네릭을 적용하기 전에, 구체 타입을 먼저 넣고, 제네릭을 치환해서 변경하는 것이 이해하기 쉽다.
 */
public class PubSub_operator_map {

    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(5).collect(toList()));
        Publisher<String> map2Pub = mapPub(pub, s -> "[ " + s + " ]");
        map2Pub.subscribe(logSub());
    }

    // T로 받아서 R로 변환 (map)
    private static Publisher<String> mapPub(Publisher<Integer> pub, Function<Integer, String> f) {
        return new Publisher<String>() {
            // logSub이 호출하는 subscribe
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                System.out.println("mapPub subscribe");
                pub.subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscriber.onSubscribe(subscription);
                    }

                    @Override
                    public void onNext(Integer item) {
                        // map과 같은 역할.
                        subscriber.onNext(f.apply(item));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
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
