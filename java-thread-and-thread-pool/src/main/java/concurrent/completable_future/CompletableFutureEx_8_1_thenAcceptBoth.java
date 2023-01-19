package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureEx_8_1_thenAcceptBoth {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello ";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("World " + Thread.currentThread().getName());
            return "World";
        }), (s1, s2) -> System.out.println(s1 + s2));

        completableFuture.get();
    }
}
