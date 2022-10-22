package com.binghe.springtransactionalandaop.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .name("빙허")
                .balance(Money.ZERO)
                .build();
    }

    @DisplayName("Customer 입금 테스트 - deposit 메서드를 통해 Customer의 잔고를 올릴 수 있다.")
    @Test
    void deposit() {
        // given
        BigDecimal plusMoney = BigDecimal.valueOf(50L);

        // when
        customer.deposit(Money.of(plusMoney));

        // then
        assertThat(customer.getBalance().getValue()).isEqualTo(plusMoney);
    }

    @DisplayName("Customer 입금 테스트 - 0보다 작은 액수는 입금할 수 없다.")
    @Test
    void deposit_lessThanZeroMoney() {
        // when, then
        assertThatThrownBy(() -> customer.deposit(Money.of(BigDecimal.valueOf(-1L))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Customer 출금 테스트 - withdraw 메서드를 통해 Customer의 잔고를 출금할 수 있다.")
    @Test
    void withdraw() {
        // given
        BigDecimal balance = BigDecimal.TEN;
        customer.deposit(Money.of(balance));

        // when
        BigDecimal withDrawMoney = BigDecimal.valueOf(5L);
        customer.withdraw(Money.of(withDrawMoney));

        // then
        assertThat(customer.getBalance().getValue()).isEqualTo(BigDecimal.valueOf(5L));
    }

    @DisplayName("Customer 출금 테스트 - 통장 잔고는 마이너스가 될 수 없다.")
    @Test
    void withdraw_balanceLessThanZero() {
        // given
        BigDecimal balance = BigDecimal.TEN;
        customer.deposit(Money.of(balance));

        // when, then
        assertThatThrownBy(() -> customer.withdraw(Money.of(BigDecimal.valueOf(100L))))
                .isInstanceOf(IllegalArgumentException.class);
    }

}