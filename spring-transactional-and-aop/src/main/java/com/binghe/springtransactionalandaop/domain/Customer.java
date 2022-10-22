package com.binghe.springtransactionalandaop.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Customer {

    private Long id;
    private String name;
    private Money balance;

    public Customer(Long id, String name, Money balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    // 입금
    public void deposit(Money money) {
        if (money == Money.ZERO || money.isMoneyLessThanZero()) {
            throw new IllegalArgumentException("0원보다 작은 액수는 입금할 수 없습니다.");
        }
        this.balance = this.balance.plus(money.getValue());
    }

    // 출금
    public void withdraw(Money money) {
        if (this.balance.minus(money.getValue()).isMoneyLessThanZero()) {
            throw new IllegalArgumentException("통장 잔고는 마이너스가 될 수 없습니다.");
        }
        this.balance = this.balance.minus(money.getValue());
    }
}
