package thread_pool.java_thread_pool.runnable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SubmitCallableExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            System.out.println("비동기적 Task");
            return "비동기적 Task 완료";
        });

        System.out.println(future.get()); // "비동기적 Task 완료"가 출력되면 Task가 정상적으로 처리되었다는 의미
        System.out.println(future.isDone()); // true

        executorService.shutdown();
    }
}
