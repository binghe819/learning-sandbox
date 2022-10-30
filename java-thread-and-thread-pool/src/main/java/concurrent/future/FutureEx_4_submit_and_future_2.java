package concurrent.future;

import thread_pool.ThreadPrintUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 이전 예시를 보면 Future.get이 Blocking 방식이므로 굳이 따로 스레드를 만들어가며 실행할 필요가 없어보인다.
 *
 * 하지만, 아래와 같이 여러 개의 작업을 병렬적으로 실행한다면 훨씬 효율적이라고 볼 수 있다. (여러 가지의 작업을 비동기로 병렬적으로 실행)
 */
public class FutureEx_4_submit_and_future_2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        // 3가지의 작업을 병렬적으로 실행시키기위해 ThreadPool에 작업들을 submit한다.
        Future<String> result1 = es.submit(() -> {
            Thread.sleep(1_000);
            return ThreadPrintUtils.getCurrentThreadName() + "Async Hello ";
        });

        Future<String> result2 = es.submit(() -> {
            Thread.sleep(2_000);
            return ThreadPrintUtils.getCurrentThreadName() + "Async Hello ";
        });

        Future<String> result3 = es.submit(() -> {
            Thread.sleep(3_000);
            return ThreadPrintUtils.getCurrentThreadName() + "Async Hello ";
        });

        System.out.println("Exit");

        // Future.get될 때, submit으로 넘긴 Callable 작업을 그제서야 실행하는 것이 아닌, ThreadPool에 submit하자마다 실행된다.
        // 여러 작업들이 비동기적으로 계속 동작하다가, get 메서드가 호출되었을때 blocking되면서 결과를 가져오는 것. (Thread.join과 유사하다)
        System.out.println(result1.get());
        System.out.println(result2.get());
        System.out.println(result3.get());

        es.shutdown();
    }
}
