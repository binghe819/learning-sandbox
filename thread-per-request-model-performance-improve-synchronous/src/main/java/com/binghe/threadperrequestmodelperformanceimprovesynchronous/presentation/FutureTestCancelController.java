package com.binghe.threadperrequestmodelperformanceimprovesynchronous.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Call;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Future와 Thread간의 관계를 파악하기위한 학습용 테스트 Controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class FutureTestController {

    private final ExecutorService asyncExecutorService;

    /**
     * # Future에 대한 cancel 테스트
     *
     * Future.cancel(true)는 비동기로 실행중인 Thread에 interrupt()를 호출할 뿐, 실제 비동기로 실행중인 스레드를 멈추진 못한다.
     *
     * 그 이유는 Thread.interrupt()가 비동기로 실행중인 스레드를 멈추는 기능은 없기때문이다. Thread.interrup()는 아래 두 가지 역할을 수행한다.
     *
     * 1. Thread.interrupte()는 wait(), join(), LockSupport.park(), sleep()으로인해 WAITING 상태인 스레드를 다시 RUNNABLE 상태로 바꾸는 역할.
     * 2. isInterrupted 상태를 변경.
     *
     * 그러므로, 비동기 작업에 Thread를 WAITING 상태로 변경하는 메서드를 사용하지 않거나, isInterrupted 상태를 사용하여 분기를 나누지않았다면 Future.cancel(true)한다고 해당 스레드가 종료되진 않는다.
     *
     */
    @GetMapping("/test/future/cancel")
    public ResponseEntity<String> future_cancel_test() {
        Future<Object> future = asyncExecutorService.submit(() -> {
            log.info("just started!");
            int count = 0;
            while (true) {
                if (count % Integer.MAX_VALUE == 0) {
                    log.info("is still working!");
                }
                count += 1;
                // do something...
            }
        });
        sleep(5_000);
        log.info("future.cancel() is called");
        future.cancel(true);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/test/future/cancel/interrupt")
    public ResponseEntity<String> future_cancel_test_interrupt() {
        Future<Object> future = asyncExecutorService.submit(new LoopTask());
        sleep(5_000);
        log.info("future.cancel() is called");
        future.cancel(true);
        return ResponseEntity.ok("ok");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class LoopTask implements Callable {
        @Override
        public Object call() throws Exception {
            log.info("just started!");
            int count = 0;
            while (!Thread.currentThread().isInterrupted()) {
                if (count % Integer.MAX_VALUE == 0) {
                    log.info("is still working!");
                }
                count += 1;
                // do something...
            }
            return "";
        }
    }
}
