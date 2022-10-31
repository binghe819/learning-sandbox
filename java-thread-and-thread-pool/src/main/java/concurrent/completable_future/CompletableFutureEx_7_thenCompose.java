package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenCompose()
 * - 두 작업이 서로 이어서 실행되도록 조합. (앞 순번 작업에 의존적이다.)
 */
public class CompletableFutureEx_7_thenCompose {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello ";
        });

        CompletableFuture<String> future = hello.thenCompose(s -> getWorld(s));

        System.out.println(future.get());
    }

    private static CompletableFuture<String> getWorld(String message) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return message + " World";
        });
    }
}
