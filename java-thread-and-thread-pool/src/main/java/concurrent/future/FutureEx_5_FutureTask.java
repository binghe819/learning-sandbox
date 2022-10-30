package concurrent.future;

import thread_pool.ThreadPrintUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask -> 비동기 실행 + Future
 */
public class FutureEx_5_FutureTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> futureTask = new FutureTask<>(() -> {
            Thread.sleep(1_000);
            return ThreadPrintUtils.getCurrentThreadName() + "Async Hello ";
        });

        es.execute(futureTask); // FutureTask는 execute만으로도 결과를 얻을 수 있따.
        System.out.println(futureTask.get());
        System.out.println("Exit");
        es.shutdown();
    }
}
