package com.binghe.springevent.application;

import com.binghe.springevent.event.RegisteredUserEvent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class RegisterUserService {

    @Autowired
    private final ApplicationEventPublisher publisher;

    public void register(RegisterUserCommand registerUserCommand) {
        // 회원 가입
        System.out.println(registerUserCommand.getName() + "님 회원 가입 요청 - DB에 영속화");

        // 가입 완료 이벤트 발행
        publisher.publishEvent(new RegisteredUserEvent(registerUserCommand.getName()));

        throw new RuntimeException("일부러 예외 터트림");
//        System.out.println(registerUserCommand.getName() + "님 가입 축하합니다. 쿠폰 번호: xxxxx");
//        System.out.println(registerUserCommand.getName() + "님 가입 축하합니다. 문자");
    }
}
