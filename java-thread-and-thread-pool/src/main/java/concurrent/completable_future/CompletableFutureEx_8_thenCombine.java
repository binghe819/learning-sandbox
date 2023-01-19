package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenCombine()
 * - 두 작업을 독립적으로 실행하고 둘 다 종료했을 때 콜백 실행
 */
public class CompletableFutureEx_8_thenCombine {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1_500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("World " + Thread.currentThread().getName());
            return "World";
        });

        CompletableFuture<String> future = hello.thenCombine(world, (h_result, w_result) -> {
            return h_result + " " + w_result;
        });

        System.out.println(future.get());
    }
}
