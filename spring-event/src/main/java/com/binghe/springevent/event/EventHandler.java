package com.binghe.springevent.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventHandler {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEmail(RegisteredUserEvent event) throws InterruptedException {
        Thread.sleep(3_000);
        System.out.println(event.getName() + "님 가입 축하합니다. 쿠폰 번호: xxxxx");
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendSMS(RegisteredUserEvent event) throws InterruptedException {
        Thread.sleep(3_000);
        System.out.println(event.getName() + "님 가입 축하합니다. 문자");
    }
}
