package com.binghe.springtransactionalandaop.service;

import com.binghe.springtransactionalandaop.AppConfiguration;
import com.binghe.springtransactionalandaop.domain.Customer;
import com.binghe.springtransactionalandaop.domain.Money;
import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 주의! 본 프로젝트는 Spring Boot를 사용하지않으므로 내장 H2가 실행되지않습니다.
 * 테스트하기전 Local에 DataSource에 명시된 DB를 띄우고 `classpath:schema.sql`를 실행하고나서 테스트하시기바랍니다.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfiguration.class)
class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        customerDao.deleteAll();
    }

    @DisplayName("송금 테스트 - 보내는 사람의 금액이 부족할 경우 송금할 수 없다. (받는 사람의 돈도 복구되어야한다.)")
    @Test
    void transfer_failed_insufficient_balance() {
        // given
        Long fromCustomerId = customerDao.add(Customer.builder().name("보내는 사람").balance(Money.of(BigDecimal.TEN)).build());
        Long toCustomerId = customerDao.add(Customer.builder().name("받는 사람").balance(Money.ZERO).build());

        // when
        assertThatThrownBy(() -> transferService.transfer(fromCustomerId, toCustomerId, Money.of(BigDecimal.valueOf(10_000))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("통장 잔고는 마이너스가 될 수 없습니다.");

        // then
        Customer fromCustomer = customerDao.findById(fromCustomerId);
        Customer toCustomer = customerDao.findById(toCustomerId);
        assertThat(fromCustomer.getBalance()).isEqualTo(Money.of(BigDecimal.TEN));
        assertThat(toCustomer.getBalance().getValue()).isEqualTo(BigDecimal.ZERO);
    }

    @DisplayName("송금 테스트 - 존재하지않는 Customer의 경우 송금할 수 없다.")
    @Test
    void transfer_non_existed_customer() {
        assertThatThrownBy(() -> transferService.transfer(-1L, -2L, Money.of(BigDecimal.TEN)))
                .isInstanceOf(RuntimeException.class);
    }
}