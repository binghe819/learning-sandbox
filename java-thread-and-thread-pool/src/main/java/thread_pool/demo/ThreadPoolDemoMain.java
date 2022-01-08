package thread_pool.demo;

public class ThreadPoolDemoMain {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(3, 10);

        for (int i = 0; i < 10; i++) {
            int taskNo = i;
            threadPool.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": Task " + taskNo);
            });
        }

        threadPool.waitUntilAllTasksFinished();
        threadPool.stop();
    }
}
