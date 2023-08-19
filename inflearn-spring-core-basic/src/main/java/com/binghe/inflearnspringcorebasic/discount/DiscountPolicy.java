package com.binghe.inflearnspringcorebasic.discount;

import com.binghe.inflearnspringcorebasic.member.Member;

public interface DiscountPolicy {

    int discount(Member member, int price);
}
