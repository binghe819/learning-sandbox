package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CompletableFutureEx_1_1_Future {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Future<String> completableFuture = calculateAsync();

        System.out.println("Main Task 실행됨");
        System.out.println("Main Task 실행됨");
        System.out.println("Main Task 실행됨");

        String completableFutureResult = completableFuture.get();

        System.out.println("Main Task 실행됨 - 2");

        System.out.println(completableFutureResult);
    }

    public static Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(() -> {
            System.out.println("비동기 요청 시작");
            Thread.sleep(5_000);
            completableFuture.complete("Hello");
            System.out.println("비동기 요청 종료");
            return null;
        });

        executorService.shutdown();

        return completableFuture;
    }
}
