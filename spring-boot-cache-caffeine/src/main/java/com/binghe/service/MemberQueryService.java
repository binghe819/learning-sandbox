package com.binghe.service;

import com.binghe.domain.Count;
import com.binghe.domain.Member;
import com.binghe.domain.MemberRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final MemberMultiKeyLocalCacheService memberMultiKeyLocalCacheService;

    public MemberQueryService(MemberRepository memberRepository,
                              MemberMultiKeyLocalCacheService memberMultiKeyLocalCacheService) {
        this.memberRepository = memberRepository;
        this.memberMultiKeyLocalCacheService = memberMultiKeyLocalCacheService;
    }

    @Cacheable(cacheNames = "member", key = "#id")
    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    // Multi-Get은 Cacheable 사용 불가.. (Caffeine Cache의 loadAll 사용하려면 각각 따로 해줘야해서 불편함)
    public List<Member> findByIds(List<Long> ids) {
        Collection<Member> members = memberMultiKeyLocalCacheService.multiCacheGet(
                "member",
                Long.class,
                Member.class,
                ids,
                keys -> memberRepository.findByIds(keys),
                (member, key) -> member.getId()
        );
        return new ArrayList<>(members);
    }

    public Count getCount() {
        return memberRepository.getCount();
    }
}
