package com.binghe.service;

import com.binghe.domain.MultiKeyCacheFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class MemberMultiKeyLocalCacheService extends MultiKeyCacheFinder {

    public MemberMultiKeyLocalCacheService(@Qualifier("localCacheManager") CacheManager cacheManager) {
        super(cacheManager);
    }
}
