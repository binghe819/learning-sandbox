package com.binghe.springbootdbreplicationjpa.application;

import com.binghe.springbootdbreplicationjpa.domain.User;
import com.binghe.springbootdbreplicationjpa.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
