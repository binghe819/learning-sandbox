package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenApply(Function)
 * - 비동기로 동작한 결과값을 리턴받아서 다른 값으로 바꾸는 콜백
 * - Function을 매개변수로 받는다.
 * - thenApplyAsync()메서드를 사용하면 원하는 Executor (스레드 풀)를 사용해서 실행할 수도 있다. (기본은 ForkJoinPool.commonPool()이라고 한다.)
 *
 * Future만으론 불가능했던 콜백을 CompletableFuture은 지원해준다.
 * 콜백 자체를 또 다른 쓰레드에서 실행할 수 있다.
 */
public class CompletableFutureEx_4_thenApply {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello ";
        }).thenApply((s) -> {
            System.out.println(Thread.currentThread().getName());
            return s.toUpperCase();
        });

        System.out.println(future.get());
    }
}
