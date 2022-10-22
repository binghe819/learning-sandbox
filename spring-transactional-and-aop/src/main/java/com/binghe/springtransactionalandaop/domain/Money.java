package com.binghe.springtransactionalandaop.domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money of(BigDecimal value) {
        if (value == BigDecimal.ZERO || BigDecimal.ZERO.compareTo(value) == 0) {
            return ZERO;
        }
        return new Money(value);
    }

    public Money plus(BigDecimal plusValue) {
        return new Money(value.add(plusValue));
    }

    public Money minus(BigDecimal minusValue) {
        return new Money(value.subtract(minusValue));
    }

    public boolean isMoneyLessThanZero() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }
}
