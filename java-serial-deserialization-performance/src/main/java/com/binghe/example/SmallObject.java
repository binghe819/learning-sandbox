package com.binghe.example;

public class SmallObject {

    private long id;
    private String name;
    private boolean active;

    public SmallObject() {
    }

    public SmallObject(long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
