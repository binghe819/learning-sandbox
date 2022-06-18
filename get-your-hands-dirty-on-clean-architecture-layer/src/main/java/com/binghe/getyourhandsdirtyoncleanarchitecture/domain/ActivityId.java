package com.binghe.getyourhandsdirtyoncleanarchitecture.domain;

public class ActivityId {

    private final Long value;

    public ActivityId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return Long.valueOf(value);
    }
}
