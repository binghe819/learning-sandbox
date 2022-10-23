package com.binghe.springtransactionalandaop.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value.longValue(), money.value.longValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
