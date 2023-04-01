package reactive_streams_scheduler;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * 여러 스레드 에서 실행되는 Reactive Streams (비동기)
 *
 * 비동기로 동작시키는 방법은 두 가지 존재함.
 * 1. publishOn
 * 2. subscribeOn
 */
public class ReactorSchedulerEx {

    public static void main(String[] args) {
        Flux.range(1, 10)
                .publishOn(Schedulers.newSingle("pub"))
                .log()
                .subscribeOn(Schedulers.newSingle("sub"))
                .subscribe(System.out::println);

        System.out.println("exit");
    }
}
