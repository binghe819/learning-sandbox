package com.binghe.inflearnspringcorebasic.order;

import com.binghe.inflearnspringcorebasic.discount.DiscountPolicy;
import com.binghe.inflearnspringcorebasic.discount.FixDiscountPolicy;
import com.binghe.inflearnspringcorebasic.member.Member;
import com.binghe.inflearnspringcorebasic.member.MemberService;
import com.binghe.inflearnspringcorebasic.member.MemberServiceImpl;

public class OrderServiceImpl implements OrderService {

    private final MemberService memberService = new MemberServiceImpl();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberService.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
