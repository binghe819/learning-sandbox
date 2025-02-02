package com.binghe.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class MemberRepository {

    private static final Map<Long, Member> db = new HashMap<>();
    private final Count count = new Count();

    static {
        db.put(1L, Member.builder().id(1L).name("binghe").address("binghe address").build());
        db.put(2L, Member.builder().id(2L).name("binghe 2").address("binghe 2 address").build());
        db.put(3L, Member.builder().id(3L).name("binghe 3").address("binghe 3 address").build());
    }

    public Member findById(Long id) {
        log.info("DB findById called. id: {}", id);
        count.countFindIdCount();
        Member member = db.get(id);

        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        return member;
    }

    public Set<Member> findByIds(Collection<Long> ids) {
        log.info("DB findByAll called. ids: {}", ids);
        count.countFindIdsCount();

        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }

        return ids.stream()
                .map(db::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Count getCount() {
        return count;
    }
}
