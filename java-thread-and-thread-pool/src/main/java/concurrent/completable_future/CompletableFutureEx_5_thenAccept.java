package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenAccept(Consumer)
 * - 비동기로 동작한 결과값을 리턴받아서 또 다른 작업을 처리하는 콜백 (리턴 없이)
 * - Comsumer를 매개변수로 받는다.
 */
public class CompletableFutureEx_5_thenAccept {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello ";
        }).thenAccept((s) -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(s.toLowerCase());
        });

        future.get();
    }
}
