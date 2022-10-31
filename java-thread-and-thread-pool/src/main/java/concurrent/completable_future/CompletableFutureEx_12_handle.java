package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * handle
 * - 정상적으로 처리되었을 때와, 예외 처리를 한번에 정의
 */
public class CompletableFutureEx_12_handle {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        boolean throwError = true;

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (throwError) {
                throw new IllegalArgumentException();
            }

            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println(ex);
                return "ERROR!";
            }
            return result;
        });

        System.out.println(future.get());
    }
}
