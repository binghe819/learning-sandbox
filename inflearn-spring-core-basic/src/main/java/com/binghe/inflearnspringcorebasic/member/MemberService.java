package com.binghe.inflearnspringcorebasic.member;

public interface MemberService {

    void join(Member member);

    Member findById(Long memberId);
}
