package com.binghe;

public class DataObject {
    private static int objectCounter = 0;

    private final String data;

    public DataObject(String data) {
        this.data = data;
    }

    public static DataObject create(String data) {
        objectCounter++;
        return new DataObject(data);
    }

    public static int getObjectCounter() {
        return objectCounter;
    }

    public String getData() {
        return data;
    }
}
