package com.binghe.springevent.application;

import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {

    public void register(RegisterUserCommand registerUserCommand) {
        // 회원 가입
        System.out.println(registerUserCommand.getName() + "님 회원 가입 완료 - DB에 영속화");

        // 가입 축하 이메일 전송 (쿠폰 포함)
        System.out.println(registerUserCommand.getName() + "님 가입 축하합니다. 쿠폰 번호: xxxxx");

        // 가입 축하 SMS 전송
        System.out.println(registerUserCommand.getName() + "님 가입 축하합니다. 문자");
    }
}
