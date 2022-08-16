package com.binghe.springresttemplate.application;

import com.binghe.springresttemplate.domain.User;
import com.binghe.springresttemplate.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(String name, int age) {
        User user = new User(name, age);
        return userRepository.save(user).getId();
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        return UserDto.createWithId(user);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::createWithId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        userRepository.delete(user);
    }

    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        user.updateName(userDto.getName());
        user.updateAge(userDto.getAge());
        return UserDto.of(user);
    }
}
