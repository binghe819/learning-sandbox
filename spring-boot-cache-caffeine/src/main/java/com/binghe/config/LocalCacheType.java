package com.binghe.config;

import com.binghe.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public enum LocalCacheType {
    MEMBER(String.class, Member.class, "member", 60L, 10L,  TimeUnit.SECONDS, 100L);

    private final Class<?> keyClass;
    private final Class<?> valueClass;
    private final String cacheName;
    private final long expiredAfterWrite;
    private final long refreshAfterWrite;
    private final TimeUnit timeUnit;
    private final long maximumSize;
}
