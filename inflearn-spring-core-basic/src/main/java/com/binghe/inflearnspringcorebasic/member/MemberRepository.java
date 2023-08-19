package com.binghe.inflearnspringcorebasic.member;

public interface MemberRepository {

    void save(Member member);

    Member findById(Long id);
}
