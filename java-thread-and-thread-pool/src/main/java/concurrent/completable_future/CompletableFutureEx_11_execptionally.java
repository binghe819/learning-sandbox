package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * exceptionally
 * - 예외 처리
 */
public class CompletableFutureEx_11_execptionally {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        boolean throwError = true;

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                throw new IllegalArgumentException();
            }

            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).exceptionally(ex -> {
            System.out.println(ex); // java.util.concurrent.CompletionException: java.lang.IllegalArgumentException
            return "ERROR!";
        });

        System.out.println(future.get()); // ERROR
    }
}
