package reactive_streams_example;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Reactive Streams - Operator
 *
 * 기존의 Reactive Streams 구조: Publisher -> Data -> Subscriber
 * Operator가 적용된 Reactive Streams 구조: Publisher -> [Data1] -> Operator1 -> [Data2] -> Operator2 -> [Data3] -> Subscriber
 *  - Operator는 Data의 변환 작업이 일어나기때문에 Transformer라고도 불리운다.
 *  - Publisher가 데이터를 보내면, 바로 Subsriber와 연동되는 것이 아닌, 여러 Operator를 거치면서 변환(가공)된 데이터가 마지막에 Subscriber까지 전달되는 구조.
 *  - Stream의 filter, map, reduce등등의 데이터 가공 작업과 유사하다. 여기선 Publisher가 전송하는 데이터를 가공하는 역할.
 *
 * 아래는 map에 대한 예시이다.
 *
 * pub -> [Data1] -> mapPub -> [Data2] -> logSub
 *                <- subscribe(logSub)
 *                -> onSubscribe(s)
 *                -> onNext()
 *                -> onNext()
 *                -> onComplete()
 * 조금 다른 그림으로 보면: map (d1 -> f -> d2)
 * TIP. pub -> sub 방향이 DownStream, 반대가 UpStream
 */
public class PubSub_operator_map {

    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(5).collect(toList()));
        Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);
        Publisher<Integer> map2Pub = mapPub(mapPub, s -> -s);
        map2Pub.subscribe(logSub());
    }

    private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f) {
        return new Publisher<Integer>() {
            // logSub이 호출하는 subscribe
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
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
