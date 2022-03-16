package com.binghe.springevent.controller;

import com.binghe.springevent.application.RegisterUserCommand;
import com.binghe.springevent.application.RegisterUserService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterUserController {

    private final RegisterUserService registerUserService;

    public RegisterUserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @GetMapping("/register/{name}")
    public ResponseEntity<String> register(@PathVariable String name) {
        registerUserService.register(new RegisterUserCommand(name));
        String successMessage = "가입 요청 200";
        System.out.println(successMessage);
        return ResponseEntity.ok(successMessage);
    }
}
