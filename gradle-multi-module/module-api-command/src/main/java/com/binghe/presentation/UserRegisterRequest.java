package com.binghe.presentation;

import lombok.Getter;

@Getter
public class UserRegisterRequest {

    private String name;
    private int age;

    public UserRegisterRequest() {
    }

    public UserRegisterRequest(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
