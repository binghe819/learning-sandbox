package com.binghe.inflearnspringcorebasic.discount;

import com.binghe.inflearnspringcorebasic.member.Grade;
import com.binghe.inflearnspringcorebasic.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1_000;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        }
        return 0;
    }
}
