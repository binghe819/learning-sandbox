package thread_pool.java_thread_pool;

import java.util.concurrent.*;

public class ScheduledExecutorExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(5);

        ScheduledFuture<String> scheduledFuture =
                scheduledExecutorService.schedule(
                        () -> {
                            System.out.println("Executed!");
                            return "Callable Result";
                        },
                        5,
                        TimeUnit.SECONDS
                );

        System.out.println("실행 결과 (5초후) : " + scheduledFuture.get());
        scheduledExecutorService.shutdown();
    }
}
