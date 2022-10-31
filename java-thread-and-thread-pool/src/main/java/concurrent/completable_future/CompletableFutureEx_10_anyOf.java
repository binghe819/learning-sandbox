package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * anyOf()
 * - 여러 작업 중에 가장 빨리 끝난 하나의 결과에 콜백을 실행한다.
 */
public class CompletableFutureEx_10_anyOf {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return "World";
        });

        CompletableFuture<Void> future = CompletableFuture.anyOf(hello, world).thenAccept(System.out::println);

        future.get();
    }
}
