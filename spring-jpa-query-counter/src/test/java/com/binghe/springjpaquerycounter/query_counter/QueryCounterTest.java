package com.binghe.springjpaquerycounter.query_counter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryCounterTest {

    @DisplayName("endCount (성공) - endCount를 하면 count 할 수 없다.")
    @Test
    void endCount_unCountable() {
        // given
        QueryCounter queryCounter = new QueryCounter();

        // when
        queryCounter.startCount();
        queryCounter.endCount();

        // then
        assertThatCode(() -> {
            queryCounter.countOne();
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("countOne (성공) - countable이 true (카운트 시작)일 때는 count할 수 있다.")
    @Test
    void countOne_Countable() {
        // given
        QueryCounter queryCounter = new QueryCounter();

        // when
        queryCounter.startCount();

        // then
        assertThatCode(() -> {
            queryCounter.countOne();
        }).doesNotThrowAnyException();
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1L);
    }

    @DisplayName("countOne (실패) - countable이 false (카운트 종료 혹은 시작전)일 때는 count할 수 없다.")
    @Test
    void countOne_unCountable() {
        // given
        QueryCounter queryCounter = new QueryCounter();

        // when, then
        assertThatCode(() -> {
            queryCounter.countOne();
        }).isInstanceOf(RuntimeException.class);
    }
}
