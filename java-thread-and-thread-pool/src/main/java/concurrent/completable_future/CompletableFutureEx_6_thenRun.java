package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenRun(Runnable)
 * - 비동기로 동작한 결과값을 리턴받지 않고 다른 작업을 처리하는 콜백.
 */
public class CompletableFutureEx_6_thenRun {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello ";
        }).thenRun(() -> {
            System.out.println("hihi");
        });

        future.get();
    }
}
