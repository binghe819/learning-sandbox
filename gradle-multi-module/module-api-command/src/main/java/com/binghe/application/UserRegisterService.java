package com.binghe.application;

import com.binghe.gradlemultimodule.domain.User;
import com.binghe.gradlemultimodule.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;

    public UserRegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long save(String name, int age) {
        User user = User.builder()
                .name(name)
                .age(age)
                .build();
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
