package com.binghe.getyourhandsdirtyoncleanarchitecture.domain;

public class AccountId {
    private final Long value;

    public AccountId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return Long.valueOf(value);
    }
}
