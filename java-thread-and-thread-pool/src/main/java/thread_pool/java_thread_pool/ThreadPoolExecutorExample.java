package thread_pool.java_thread_pool;

import java.util.concurrent.*;

/**
   ThreadPoolExecutor 테스트
 */
public class ThreadPoolExecutorExample {

    public static void main(String[] args) {
        // thread pool setting
        int  corePoolSize  =    5;
        int  maxPoolSize   =   10;
        long keepAliveTime = 5000;

        ExecutorService threadPoolExecutor =
                new ThreadPoolExecutor(
                        corePoolSize,
                        maxPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );

        // use thread pool
        threadPoolExecutor.execute(new Task("Task 1"));
        threadPoolExecutor.execute(new Task("Task 2"));
        threadPoolExecutor.execute(new Task("Task 3"));
        threadPoolExecutor.execute(new Task("Task 4"));
        threadPoolExecutor.execute(new Task("Task 5"));

        // shutdown thread pool
        threadPoolExecutor.shutdown();
    }
}
