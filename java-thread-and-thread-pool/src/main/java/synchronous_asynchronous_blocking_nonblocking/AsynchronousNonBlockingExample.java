package synchronous_asynchronous_blocking_nonblocking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsynchronousNonBlockingExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        System.out.println("Task A 실행 시작!");

        // Non-Blocking으로 작업 요청.
        es.execute(() -> {
            // 아래 작업의 완료 여부는 Main 스레드가 전혀 신경쓰지 않는다. (결과가 전혀 필요없으므로)
            try {
                System.out.println("Task B 실행");
                Thread.sleep(1_000);
                System.out.println("Task B 완료");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Task A 작업은 계속 진행");
        System.out.println("Task A 작업은 계속 진행");
        System.out.println("Task A 작업은 계속 완료");
        es.shutdown();
    }
}
