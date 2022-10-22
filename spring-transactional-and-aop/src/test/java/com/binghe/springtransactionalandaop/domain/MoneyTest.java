package com.binghe.springtransactionalandaop.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @DisplayName("Money 생성 테스트")
    @Test
    void Money_create() {
        // given
        BigDecimal value = BigDecimal.valueOf(100_000L);

        // when
        Money money = Money.of(value);

        // then
        assertThat(money.getValue()).isEqualTo(value);
    }

    @DisplayName("Money 생성 테스트 - 0원이면 ZERO 객체를 반환한다.")
    @Test
    void Money_create_zero() {
        // given
        BigDecimal value1 = BigDecimal.ZERO;
        BigDecimal value2 = BigDecimal.valueOf(0L);

        // when
        Money money1 = Money.of(value1);
        Money money2 = Money.of(value2);

        // then
        assertThat(money1).isSameAs(Money.ZERO);
        assertThat(money2).isSameAs(Money.ZERO);
    }

    @DisplayName("Money plus 테스트 - Money는 플러스 연산을 할 수 있다.")
    @Test
    void Money_plus() {
        // given
        Money money = Money.ZERO;
        BigDecimal plusValue = BigDecimal.valueOf(100_000L);

        // when
        Money result = money.plus(plusValue);

        // then
        assertThat(result.getValue()).isEqualTo(plusValue);
    }

    @DisplayName("Money minus 테스트 - Money는 빼기 연산을 할 수 있다.")
    @Test
    void Money_minus() {
        // given
        Money money = Money.of(BigDecimal.valueOf(100L));

        // when
        Money result = money.minus(BigDecimal.valueOf(50L));

        // then
        assertThat(result.getValue()).isEqualTo(BigDecimal.valueOf(50L));
    }

    @DisplayName("Money isMoneyLessThanZero 테스트 - Money는 음수인지 확인할 수 있다.")
    @Test
    void Money_isMoneyLessThanZero() {
        // given, then
        Money result = Money.ZERO.minus(BigDecimal.valueOf(50L));
        Money result2 = Money.ZERO.plus(BigDecimal.valueOf(50L));

        // then
        assertThat(result.isMoneyLessThanZero()).isTrue();
        assertThat(result2.isMoneyLessThanZero()).isFalse();
    }
}