package concurrent.completable_future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CompletableFutureEx_15_orTimeout {

    private static final Logger log = LoggerFactory.getLogger(CompletableFutureEx_15_orTimeout.class);

    public static void main(String[] args) {
        ExecutorService es = Executors.newSingleThreadExecutor();

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            sleep(3_000); // 오랫동안 동작하는 작업...

            log.info("여기가 실행되지 않을 것이라고 예상되지만, 실제론 실행됩니다.");

            return "finished";
        }, es)
        .orTimeout(1_000, TimeUnit.MILLISECONDS) // 1초 타임아웃 설정
        .exceptionally(ex -> {
            return "default value";
        });

        String result = completableFuture.join();
        log.info(result); // default value

        es.shutdown();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
