package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureEx_9_2_allOf {

    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture<Void> combinedFutures = CompletableFuture.allOf(future1, future2)
                .thenAccept(System.out::println);

        System.out.println(combinedFutures);
    }
}
