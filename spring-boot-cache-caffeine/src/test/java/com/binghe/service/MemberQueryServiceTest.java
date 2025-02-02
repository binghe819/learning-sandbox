package com.binghe.service;

import com.binghe.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberQueryServiceTest {

    @Autowired
    private MemberQueryService memberQueryService;

    @Test
    void findById_cache() {
        memberQueryService.findById(1L);
        memberQueryService.findById(1L);

        assertThat(memberQueryService.getCount().getFindByIdCount()).isEqualTo(1);
        assertThat(memberQueryService.getCount().getFindByIdsCount()).isEqualTo(0);
    }

    @Test
    void findByids_cache() {
    }
}