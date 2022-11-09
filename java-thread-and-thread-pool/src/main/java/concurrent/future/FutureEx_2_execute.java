package concurrent.future;

import thread_pool.ThreadPrintUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 비동기 예시 1
 * - Executor의 execute(Runnable)을 통해 결과가 없는 비동기 실행.
 * - return 값이 없다.
 * - 내부에서 예외를 처리해주어야한다. (ex. Thread.sleep에서의 InterruptedException)
 */
public class FutureEx_2_execute {

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();

        es.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            System.out.println(ThreadPrintUtils.getCurrentThreadName() + " Start");
        });

        es.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
            System.out.println(ThreadPrintUtils.getCurrentThreadName() + " Process");
        });

        es.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            System.out.println(ThreadPrintUtils.getCurrentThreadName() + " End");
        });

        System.out.println(ThreadPrintUtils.getCurrentThreadName() + " Exit");

        es.shutdown();
    }
}
