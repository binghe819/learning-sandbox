package concurrent.future;

import thread_pool.ThreadPrintUtils;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask를 이용하면 비동기 작업이 완료되면 같이 넘긴 Callback을 실행하라고 할 수 있다.
 * - 이때 Main 스레드에서 비동기 작업의 Future.get을 호출하는 것이 아니기에, Main은 Blocking되지 않는다.
 * - 예외 처리도 예외 처리 Callback을 넘겨서 비동기 수행중 발생한 예외를 처리할 수 있게한다.
 */
public class FutureEx_7_FutureTask_Callback {
    interface SuccessCallback {
        public void onSuccess(String result);
    }

    interface ExceptionCallback {
        public void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        // Callback 처리 메서드.
        @Override
        protected void done() {
            try {
                // get이 여기서 비동기로 동작하는 스레드에서 실행되기때문에, 기존의 Main 스레드는 Blocking 되지 않는다.
                sc.onSuccess(get());
            } catch (InterruptedException e) {
                // 인터럽트가 발생했을때의 예외 처리
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                // 비동기 작업중 발생한 예외 처리.
                ec.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask callbackFutureTask = new CallbackFutureTask(() -> {
            Thread.sleep(1_000);
//            if (1==1) throw new RuntimeException("비동기 처리중 Error!!");
            return ThreadPrintUtils.getCurrentThreadName() + "Async Hello ";
        },
        s -> System.out.println("Result : " + s),
        e -> System.out.println("Error : " + e.getMessage()));

        es.execute(callbackFutureTask);
        System.out.println("Exit");
        es.shutdown();
    }
}
