package com.binghe.domain;

import org.junit.jupiter.api.Test;

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
        List<Member> members = repository.findByIds(List.of(1L, 2L));

        assertThat(members).isNotEmpty();
        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(0).getId()).isEqualTo(1L);
        assertThat(members.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void findByIds_empty() {
        List<Member> members = repository.findByIds(List.of(1000L));

        assertThat(members).isEmpty();
    }
}