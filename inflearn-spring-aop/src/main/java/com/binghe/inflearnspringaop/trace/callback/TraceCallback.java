package com.binghe.inflearnspringaop.trace.callback;

public interface TraceCallback<T> {
    T call();
}
