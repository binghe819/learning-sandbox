package com.binghe.presentation;

import com.binghe.application.UserReadService;
import com.binghe.gradlemultimodule.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserReadController {

    private final UserReadService userReadService;

    public UserReadController(UserReadService userReadService) {
        this.userReadService = userReadService;
    }

    @GetMapping(value = "/api/users/{id}")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) {
        User user = userReadService.findById(id);
        UserInfoResponse response = new UserInfoResponse(user.getId(), user.getName(), user.getAge());
        return ResponseEntity
                .ok()
                .body(response);
    }
}
