package asynchronism;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FutureTest {

    @DisplayName("동기 - ")
    @Test
    void Synchronizied_task() throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Thread.sleep(2000);
        System.out.println("Hello ");

        System.out.println("Exit");
    }

    @DisplayName("비동기 - ")
    @Test
    void Executor_execute() throws InterruptedException {
        // given
        ExecutorService es = Executors.newCachedThreadPool();

        es.execute(() -> {
            System.out.println("");
        });

        Thread.sleep(2000);
        System.out.println("Hello ");

        System.out.println("Exit");

    }
}
