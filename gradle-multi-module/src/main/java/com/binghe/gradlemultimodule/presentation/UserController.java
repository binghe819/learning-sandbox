package com.binghe.gradlemultimodule.presentation;

import com.binghe.gradlemultimodule.application.UserService;
import com.binghe.gradlemultimodule.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/api/users/{id}")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserInfoResponse response = new UserInfoResponse(user.getId(), user.getName(), user.getAge());
        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping(value = "/api/users")
    public ResponseEntity<Void> save(@RequestBody UserRegisterRequest body) {
        Long savedId = userService.save(body.getName(), body.getAge());
        return ResponseEntity.created(URI.create("/users/" + savedId)).build();
    }
}
