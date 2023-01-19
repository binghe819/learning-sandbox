package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CompletableFutureEx_9_4_allOf {

    public static void main(String[] args) {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return "World";
        });

    }
}
