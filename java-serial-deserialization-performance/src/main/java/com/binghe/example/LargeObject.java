package com.binghe.example;

import java.util.List;

public class LargeObject {

    private long id;
    private String name;
    private List<SmallObject> smallObjects;

    public LargeObject() {
    }

    public LargeObject(long id, String name, List<SmallObject> smallObjects) {
        this.id = id;
        this.name = name;
        this.smallObjects = smallObjects;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SmallObject> getSmallObjects() {
        return smallObjects;
    }
}
