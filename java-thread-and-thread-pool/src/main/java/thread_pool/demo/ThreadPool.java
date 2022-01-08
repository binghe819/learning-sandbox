package thread_pool.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// ThreadPool에 접근하는 인터페이스와 같은 역할
public class ThreadPool {

    private BlockingQueue taskQueue;
    private List<PoolThreadRunnable> runnables = new ArrayList<>();
    private boolean isStoped = false;

    private ThreadPool() {}

    public ThreadPool(int numOfThreads, int maxNumOfTasks) {
        taskQueue = new ArrayBlockingQueue(maxNumOfTasks);

        for (int i = 0; i < numOfThreads; i++) {
            PoolThreadRunnable poolThreadRunnable = new PoolThreadRunnable(taskQueue);

            runnables.add(poolThreadRunnable);
        }

        for (PoolThreadRunnable runnable : runnables) {
            new Thread(runnable).start();
        }
    }

    public synchronized void execute(Runnable task) {
        if (this.isStoped) {
            throw new IllegalStateException("Thread Pool is Stopped");
        }
        this.taskQueue.offer(task);
    }

    public synchronized void stop() {
        this.isStoped = true;
        for (PoolThreadRunnable runnable : runnables) {
            runnable.doStop();
        }
    }

    public synchronized void waitUntilAllTasksFinished() {
        while (this.taskQueue.size() > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
