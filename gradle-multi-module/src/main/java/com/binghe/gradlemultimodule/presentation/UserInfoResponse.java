package com.binghe.gradlemultimodule.presentation;

import lombok.Getter;

@Getter
public class UserInfoResponse {

    private Long id;
    private String name;
    private int age;

    public UserInfoResponse() {
    }

    public UserInfoResponse(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
