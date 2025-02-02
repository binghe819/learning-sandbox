package com.binghe.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        memberQueryService.findByIds(List.of(1L, 2L));

        assertThat(memberQueryService.getCount().getFindByIdsCount()).isEqualTo(1L);
        assertThat(memberQueryService.getCount().getFindByIdCount()).isEqualTo(0L);
        memberQueryService.getCount().reset();

        memberQueryService.findByIds(List.of(1L, 2L, 3L));

        assertThat(memberQueryService.getCount().getFindByIdsCount()).isEqualTo(1L);
        assertThat(memberQueryService.getCount().getFindByIdCount()).isEqualTo(0L);
        memberQueryService.getCount().reset();

        memberQueryService.findByIds(List.of(1L, 2L, 3L));
        assertThat(memberQueryService.getCount().getFindByIdsCount()).isEqualTo(0L);
        assertThat(memberQueryService.getCount().getFindByIdCount()).isEqualTo(0L);
    }
}