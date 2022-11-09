package concurrent.future;

import thread_pool.ThreadPrintUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 동기 예시
 */
public class FutureEx_1_synchronism {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        printAfterSeconds("Start", 3000);
        printAfterSeconds("In Progress", 2000);
        printAfterSeconds("End", 0);

        es.shutdown();
    }

    private static void printAfterSeconds(String text, long millis) throws InterruptedException {
        Thread.sleep(millis);
        System.out.println(ThreadPrintUtils.getCurrentThreadName() + " " + text);
    }
}
