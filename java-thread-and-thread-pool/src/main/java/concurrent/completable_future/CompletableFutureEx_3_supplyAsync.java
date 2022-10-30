package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * supplyAsync()
 * - 비동기적으로 동작하길 원하는 작업중 리턴값이 있는 경우 사용된다.
 * - 원하는 Executor (스레드 풀)를 사용해서 실행할 수도 있다. (기본은 ForkJoinPool.commonPool()이라고 한다.)
 */
public class CompletableFutureEx_3_supplyAsync {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello " + Thread.currentThread().getName(), es);

        // get()을 호출해야지만, 비동기적으로 동작한다.
        System.out.println(future.get());

        es.shutdown();
    }
}
