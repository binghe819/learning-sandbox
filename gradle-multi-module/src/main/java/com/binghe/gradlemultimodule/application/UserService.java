package com.binghe.gradlemultimodule.application;

import com.binghe.gradlemultimodule.domain.User;
import com.binghe.gradlemultimodule.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("에러! 찾을 수 없는 유저입니다."));
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
