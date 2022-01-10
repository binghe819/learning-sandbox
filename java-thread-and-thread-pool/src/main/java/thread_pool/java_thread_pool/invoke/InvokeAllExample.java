package thread_pool.java_thread_pool.invoke;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class InvokeAllExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Set<Callable<String>> callables = new HashSet<>();
        callables.add(() -> "Task 1 Completed");
        callables.add(() -> "Task 2 Completed");
        callables.add(() -> "Task 3 Completed");

        List<Future<String>> futures = executorService.invokeAll(callables);

        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        executorService.shutdown();
    }
}
