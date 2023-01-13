package synchronous_asynchronous_blocking_nonblocking;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SynchronousNonBlockingExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        // Task A 비동기 실행 - 10초동안 I/O 작업하는 Task (라고 생각하자)
        System.out.println("Task A 실행 시작!");
        Future<String> task_a = es.submit(() -> returnValueAfterSeconds("Task A (Kernel I/O)", 10_000));

        // Task A가 종료되었는지 확인
        while(!task_a.isDone()) {
            System.out.println("Task A가 완료되었는지 계속 확인.");
            // Task A가 완료되지않아도 여기에서 다른 작업을 계속 수행.
            printAfterSeconds("Task B (Application 작업)", 2_000);
        }

        // Task A의 작업이 완료되면 작업 결과에 따른 다른 작업 처리.
        System.out.println(task_a.get() + " 실행 종료!");

        es.shutdown();
    }

    private static void printAfterSeconds(String text, long millis) throws InterruptedException {
        Thread.sleep(millis);
        System.out.println(text);
    }

    private static String returnValueAfterSeconds(String text, long millis) throws InterruptedException {
        Thread.sleep(millis);
        return text;
    }
}
