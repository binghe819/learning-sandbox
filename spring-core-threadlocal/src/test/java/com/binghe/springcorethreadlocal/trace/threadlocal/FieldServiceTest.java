package com.binghe.springcorethreadlocal.trace.threadlocal;

import com.binghe.springcorethreadlocal.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 동시성 이슈 발생 간단 테스트
 */
@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> fieldService.logic("userA");
        Runnable userB = () -> fieldService.logic("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2_000); // 동시성 문제 발생 X
        sleep(10); // 동시성 문제 발생 O
        threadB.start();

        sleep(2_000); // main 스레드를 죽이지 않기위한 sleep (테스트 코드의 경우 모든 스레드가 데몬 스레드로 동작하는 듯하다.)
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
