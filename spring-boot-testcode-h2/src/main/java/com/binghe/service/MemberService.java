package com.binghe.service;

import com.binghe.domain.Member;
import com.binghe.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.getAll();
    }

    public void deleteAllMembers() {
        memberRepository.deleteAll();
    }

    public int getMemberCount() {
        return memberRepository.getCount();
    }

    public void updateMember(Member member) {
        memberRepository.update(member);
    }
}

