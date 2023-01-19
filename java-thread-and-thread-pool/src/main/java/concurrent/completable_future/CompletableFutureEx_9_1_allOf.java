package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureEx_9_1_allOf {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> futuru1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> futuru2 = CompletableFuture.supplyAsync(() -> "Beautiful");
        CompletableFuture<String> futuru3 = CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futuru1, futuru2, futuru3);

        // ... 비동기 요청후 따로 처리해야할 코드가 있으면 여기에 정의하면 됩니다 ...

        combinedFuture.get();

        System.out.println(futuru1.isDone());
        System.out.println(futuru2.isDone());
        System.out.println(futuru3.isDone());
    }
}
