package com.binghe.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Long save(Member member);
    Optional<Member> findById(Long id);
    List<Member> getAll();
    void deleteAll();
    int getCount();
    void update(Member member);
}
