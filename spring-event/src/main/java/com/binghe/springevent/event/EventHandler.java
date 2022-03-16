package com.binghe.springevent.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    @EventListener
    public void sendEmail(RegisteredUserEvent event) {
        System.out.println(event.getName() + "님 가입 축하합니다. 쿠폰 번호: xxxxx");
    }

    @EventListener
    public void sendSMS(RegisteredUserEvent event) {
        System.out.println(event.getName() + "님 가입 축하합니다. 문자");
    }
}
