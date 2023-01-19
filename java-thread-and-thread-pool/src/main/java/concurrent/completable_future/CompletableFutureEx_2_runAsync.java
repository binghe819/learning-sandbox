package concurrent.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.*;

/**
 * runAsync() 메서드
 * - 비동기적으로 동작하길 원하는 작업중 리턴값이 없는 경우 사용된다.
 */
public class CompletableFutureEx_2_runAsync {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Hello " + currentThread().getName());
            System.out.println("TESTTESTTEST");
        });

        // get()을 호출해야지만, 비동기적으로 동작한다.
        future.get();
    }
}
