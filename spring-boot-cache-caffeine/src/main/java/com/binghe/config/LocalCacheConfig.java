package com.binghe.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableCaching
@Configuration
public class LocalCacheConfig {

    @Bean
    public List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(LocalCacheType.values())
                .map(info -> {
                    Cache<Object, Object> cache = Caffeine.newBuilder()
                            .expireAfterWrite(info.getExpiredAfterWrite(), info.getTimeUnit())
                            // refreshAfterWrite는 기본적으로 LoadingCache를 사용해야함.
//                            .refreshAfterWrite(info.getRefreshAfterWrite(), info.getTimeUnit())
                            .maximumSize(info.getMaximumSize())
                            .build();

                    return new CaffeineCache(info.getCacheName(), cache);
                }).collect(Collectors.toList());
    }

    @Bean
    public CacheManager localCacheManager(List<CaffeineCache> caffeineCaches) {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(caffeineCaches);

        return simpleCacheManager;
    }
}
