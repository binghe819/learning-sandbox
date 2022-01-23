package com.binghe.presentation;

import com.binghe.application.UserRegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UserRegisterController {

    private final UserRegisterService userRegisterService;

    public UserRegisterController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @PostMapping(value = "/api/users")
    public ResponseEntity<Void> save(@RequestBody UserRegisterRequest body) {
        Long savedId = userRegisterService.save(body.getName(), body.getAge());
        return ResponseEntity.created(URI.create("/users/" + savedId)).build();
    }
}
