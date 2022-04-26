package com.binghe.springbootjpaexample2.shoppin_mall.application;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.Address;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Member;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
//    @Rollback(false)
    void 회원가입_성공() {
        // given
        String name = "binghe";
        Address address = new Address("seoul", "oksu", "12312");
        Member member = new Member(null, name, address, null);

        // when
        Long savedMemberId = memberService.register(member);
        Member foundMember = memberRepository.findById(savedMemberId);

        // then
        assertThat(member).isSameAs(foundMember); // 하나의 트랜잭션 안에서 동작하기에 동일
    }

    @Test
    void 회원가입_실패() {
        // given
        String name = "binghe";
        Address address = new Address("seoul", "oksu", "12312");
        Member member1 = new Member(null, name, address, null);
        Member member2 = new Member(null, name, address, null);

        // when
        memberService.register(member1);

        // then
        assertThatCode(() -> memberService.register(member2))
                .isInstanceOf(IllegalStateException.class);
    }
}