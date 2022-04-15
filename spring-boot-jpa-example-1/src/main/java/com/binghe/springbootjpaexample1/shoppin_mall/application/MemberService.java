package com.binghe.springbootjpaexample1.shoppin_mall.application;

import com.binghe.springbootjpaexample1.shoppin_mall.domain.Member;
import com.binghe.springbootjpaexample1.shoppin_mall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long register(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 동시성을 고려하여 DB의 name에 unique 설정을 주는 것이 좋다.
        List<Member> findMembers = memberRepository.findAllByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }
}
