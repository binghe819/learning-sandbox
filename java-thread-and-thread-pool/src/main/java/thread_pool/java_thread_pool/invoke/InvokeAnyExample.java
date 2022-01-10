package thread_pool.java_thread_pool.invoke;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InvokeAnyExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Set<Callable<String>> callables = new HashSet<>();
        callables.add(() -> "Task 1 Completed");
        callables.add(() -> "Task 2 Completed");
        callables.add(() -> "Task 3 Completed");

        String result = executorService.invokeAny(callables);
        System.out.println(result);

        executorService.shutdown();
    }
}
