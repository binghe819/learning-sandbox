package com.binghe.inflearnspringcorebasic.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    void join() {
        // given
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when
        memberService.join(member);
        Member findMember = memberService.findById(1L);

        // then
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getGrade()).isEqualTo(member.getGrade());
    }
}
