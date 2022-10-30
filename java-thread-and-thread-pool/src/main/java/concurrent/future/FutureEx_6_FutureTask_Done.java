package concurrent.future;

import thread_pool.ThreadPrintUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask의 done 메서드를 이용한 비동기 처리 결과 얻는 방법. (실제로는 사용되지 않을 듯 하다.)
 */
public class FutureEx_6_FutureTask_Done {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> futureTask = new FutureTask<>(() -> {
            Thread.sleep(1_000);
            return ThreadPrintUtils.getCurrentThreadName() + "Async Hello ";
        }) {
            // 비동기작업이 모두 수행하고나면 호출되는 메서드. (hook 역할)
            @Override
            protected void done() {
                try {
                    System.out.println(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(futureTask);
        System.out.println("Exit");
        es.shutdown();
    }
}
