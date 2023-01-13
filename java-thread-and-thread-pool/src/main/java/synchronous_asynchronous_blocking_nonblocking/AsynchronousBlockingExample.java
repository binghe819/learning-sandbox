package synchronous_asynchronous_blocking_nonblocking;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsynchronousBlockingExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        // Non-Blocking 방식처럼 Task 요청.
        Future<String> taskA = es.submit(() -> {
            Thread.sleep(3000);
            return "Task A Completed";
        });

        // Task 요청하자마자 Blocking 되어 TaskA가 완료되어 신호를 주기전까지 계속 Blocking 상태가된다.
        String taskAResult = taskA.get();
        System.out.println(taskAResult);

        es.shutdown();
    }
}
