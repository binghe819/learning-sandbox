package concurrent.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 동기 예시
 */
public class FutureEx_1_synchronism {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Thread.sleep(2000);
        System.out.println("Hello ");

        System.out.println("Exit");

        es.shutdown();
    }
}
