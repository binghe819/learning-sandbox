package com.binghe.domain;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MemberRepositoryTest {

    private final MemberRepository repository = new MemberRepository();

    @Test
    void findById_success() {
        Member member = repository.findById(1L);

        assertThat(member).isNotNull();
        assertThat(member.getId()).isEqualTo(1L);
    }

    @Test
    void findById_fail() {
        assertThatCode(() -> repository.findById(0L))
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @Test
    void findByIds() {
        Collection<Member> members = repository.findByIds(List.of(1L, 2L));

        assertThat(members).isNotEmpty();
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    void findByIds_empty() {
        Collection<Member> members = repository.findByIds(List.of(1000L));

        assertThat(members).isEmpty();
    }
}