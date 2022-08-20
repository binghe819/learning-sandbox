package com.binghe.springresttemplate.user.application;

import com.binghe.springresttemplate.user.domain.User;
import lombok.Getter;

@Getter
public class UserDto {

    private Long id;
    private String name;
    private int age;

    public UserDto() {
    }

    public UserDto(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public static UserDto of(User user) {
        return new UserDto(user.getId(), user.getName(), user.getAge());
    }

    public static UserDto createWithId(User user) {
        return new UserDto(user.getId(), user.getName(), user.getAge());
    }
}
