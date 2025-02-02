package com.binghe.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public abstract class MultiKeyCacheFinder {

    private final CacheManager cacheManager;

    public MultiKeyCacheFinder(CacheManager cacheManager) {
        if (cacheManager == null) {
            throw new IllegalArgumentException("cacheManager는 null이 될 수 없습니다.");
        }
        this.cacheManager = cacheManager;
    }

    public <K, V> Collection<V> multiCacheGet(String cacheKeyName,
                                       Class<K> keyClass,
                                       Class<V> valueClass,
                                       Collection<K> keys,
                                       Function<Set<K>, Set<V>> notCachedValueFinder,
                                       BiFunction<V, Class<K>, K> keyExtractor) {
        Cache cache = getCache(cacheKeyName);

        final Set<K> notCachedKeys = new HashSet<>();
        final Set<V> cachedValues = new HashSet<>();

        for (K key : keys) {
            V cachedValue = cache.get(key, valueClass);
            if (cachedValue != null) {
                cachedValues.add(cachedValue);
            } else {
                notCachedKeys.add(key);
            }
        }

        log.info("캐싱되지 않은 키값: {}", notCachedKeys);

        Set<V> loadedValues = loadNotCachedValues(cache, keyClass, notCachedKeys, notCachedValueFinder, keyExtractor);

        cachedValues.addAll(loadedValues);
        return cachedValues;
    }

    private Cache getCache(String cacheKeyName) {
        return Optional.ofNullable(cacheManager.getCache(cacheKeyName))
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("선언되지 않은 cache 이름입니다. cacheName: %s", cacheKeyName)));
    }

    private <K, V> Set<V> loadNotCachedValues(Cache cache,
                                              Class<K> keyClass,
                                              Set<K> notCachedKeys,
                                              Function<Set<K>, Set<V>> notCachedValueFinder,
                                              BiFunction<V, Class<K>, K> keyExtractor) {
        if (notCachedKeys.isEmpty()) {
            return Collections.unmodifiableSet(new HashSet<>());
        }

        Set<V> loadedValues = notCachedValueFinder.apply(notCachedKeys);

        for (V value : loadedValues) {
            cache.put(keyExtractor.apply(value, keyClass), value);
        }
        return loadedValues;
    }
}
