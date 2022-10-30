package thread_pool.asynchronism;

import thread_pool.ThreadPrintUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 비동기 예시 2
 * - ExecutorService의 submit을 통해 결과가 있는 비동기 실행.
 * - return 값이 존재한다.
 *
 *
 * Future
 * - 비동기적인 연산을 수행하고난 결과를 가지고있는 추상화 객체.
 */
public class FutureEx_3_submit_and_future {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> result = es.submit(() -> {
            Thread.sleep(5_000);
            return ThreadPrintUtils.getCurrentThreadName() + "Async Hello ";
        });

        // 비동기 작업의 결과를 받아서 출력.
        // 중요한 점: Future.get을 호출하면 해당 Future의 비동기 작업이 끝날 때까지 현재의 스레드는 블록된다.
        //    - Future.get은 Blocking 메서드.
        // 즉, 비동기 작업의 결과가 나올때까지 이 줄에서 대기한다.
        System.out.println(result.get());
        System.out.println(ThreadPrintUtils.getCurrentThreadName() + "Exit");

        es.shutdown();
    }
}
