package com.binghe.springdocopenapiswagger.user.application;

import lombok.Getter;

import java.util.List;

@Getter
public class UserDtos {

    private List<UserDto> userDtos;

    public UserDtos() {
    }

    public UserDtos(List<UserDto> userDtos) {
        this.userDtos = userDtos;
    }
}
