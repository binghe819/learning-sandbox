package com.binghe;

public interface DataProcessor {

    byte[] getBytes(Object obj);

    <T> T getObject(byte[] data, Class<T> tClass);
}
