package com.binghe.service;

import com.binghe.domain.Member;
import com.binghe.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Cacheable(value = "members", key="#id")
    @Transactional(readOnly = true)
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.getAll();
    }

    @Transactional
    public void deleteAllMembers() {
        memberRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public int getMemberCount() {
        return memberRepository.getCount();
    }

    @Transactional
    public void updateMember(Member member) {
        memberRepository.update(member);
    }
}

