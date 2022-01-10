package thread_pool.java_thread_pool.runnable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SubmitRunnableExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<?> future = executorService.submit(() -> {
            System.out.println("비동기 Task");
        });

        System.out.println(future.get()); // null이 나오면 Task가 정상적으로 처리되었다는 의미.
        System.out.println(future.isDone()); // true

        executorService.shutdown();
    }
}
