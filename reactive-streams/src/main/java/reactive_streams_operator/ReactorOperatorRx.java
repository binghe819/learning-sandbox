package reactive_streams_operator;

import reactor.core.publisher.Flux;

/**
 * Reactive Streams의 구현체인 Reactor를 이용하여 Operator를 실행해보는 예시
 *
 * (map과 reduce등을 하나씩 주석해가면서 어떻게 동작하는지 로그를 통해 살펴보자.)
 *
 * Stream의 여러 연산 (map..)과 다른 점은 Reactive Streams Operator는 비동기 기반으로 동작한다.
 * (또한, Push 방식임)
 *
 * - https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html
 */
public class ReactorOperatorRx {

    public static void main(String[] args) {
        Flux.<Integer>create(e -> {
            e.next(1);
            e.next(2);
            e.next(3);
            e.next(4);
            e.complete();
        })
        .log()
        .map(s -> s * 10)
        .log()
        .reduce(0, (a, b) -> a + b)
        .log()
        .subscribe(System.out::println);
    }
}
