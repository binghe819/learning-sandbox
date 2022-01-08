package thread_pool.java_thread_pool.simple_example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExampleMain {

    public static void main(String[] args) {
        // given
        Runnable task1 = new Task("Task 1");
        Runnable task2 = new Task("Task 2");
        Runnable task3 = new Task("Task 3");
        Runnable task4 = new Task("Task 4");
        Runnable task5 = new Task("Task 5");

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        // when
        threadPool.execute(task1);
        threadPool.execute(task2);
        threadPool.execute(task3);
        threadPool.execute(task4);
        threadPool.execute(task5);

        // and
        threadPool.shutdown();
    }
}
