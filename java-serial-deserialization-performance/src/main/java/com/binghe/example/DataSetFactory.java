package com.binghe.example;

import java.util.ArrayList;
import java.util.List;

public class DataSetFactory {

    public static SmallObject createSmallObject() {
        return new SmallObject(1L, "binghe", true);
    }

    public static LargeObject createLargeObject() {
        List<SmallObject> smallObjects = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            smallObjects.add(new SmallObject(i, "binghe" + i, true));
        }
        return new LargeObject(1L, "binghe", smallObjects);
    }

    public static com.binghe.thrift.SmallObject createSmallObjectThrift() {
        return new com.binghe.thrift.SmallObject(1L, "binghe", true);
    }

    public static com.binghe.thrift.LargeObject createLargeObjectThrift() {
        List<com.binghe.thrift.SmallObject> smallObjects = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            smallObjects.add(new com.binghe.thrift.SmallObject(i, "binghe" + i, true));
        }
        return new com.binghe.thrift.LargeObject(1L, "binghe", smallObjects);
    }
}
