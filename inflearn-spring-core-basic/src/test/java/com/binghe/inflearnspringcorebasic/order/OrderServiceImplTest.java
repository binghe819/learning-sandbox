package com.binghe.inflearnspringcorebasic.order;

import com.binghe.inflearnspringcorebasic.member.Grade;
import com.binghe.inflearnspringcorebasic.member.Member;
import com.binghe.inflearnspringcorebasic.member.MemberService;
import com.binghe.inflearnspringcorebasic.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceImplTest {

    MemberService memberService = new MemberServiceImpl();
    OrderServiceImpl orderService = new OrderServiceImpl();

    @Test
    void order_VIP() {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "binghe", Grade.VIP);
        memberService.join(member);

        // when
        Order order = orderService.createOrder(memberId, "itemA", 10_000);

        // then
        assertThat(order.calculatePrice()).isEqualTo(9_000);
    }

    @Test
    void order_NORMAL() {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "binghe", Grade.NORMAL);
        memberService.join(member);

        // when
        Order order = orderService.createOrder(memberId, "itemA", 10_000);

        // then
        assertThat(order.calculatePrice()).isEqualTo(10_000);
    }
}