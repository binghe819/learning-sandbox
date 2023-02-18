package com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AutoReleaseCompletableFuture<T> extends CompletableFuture {

    @Override
    public boolean complete(Object value) {
        if (isDone()) {
            return false;
        }

        return super.complete(value);
    }

    @Override
    public CompletableFuture completeOnTimeout(Object value, long timeout, TimeUnit unit) {
        Thread.currentThread().interrupt();
        return super.completeOnTimeout(value, timeout, unit);
    }

    @Override
    public CompletableFuture orTimeout(long timeout, TimeUnit unit) {
        Thread.currentThread().interrupt();
        return super.orTimeout(timeout, unit);
    }
}
