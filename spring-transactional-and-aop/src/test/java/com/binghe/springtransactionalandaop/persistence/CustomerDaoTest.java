package com.binghe.springtransactionalandaop.persistence;

import com.binghe.springtransactionalandaop.AppConfiguration;
import com.binghe.springtransactionalandaop.domain.Customer;
import com.binghe.springtransactionalandaop.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 주의! 본 프로젝트는 Spring Boot를 사용하지않으므로 내장 H2가 실행되지않습니다.
 * 테스트하기전 Local에 DataSource에 명시된 DB를 띄우고 `classpath:schema.sql`를 실행하고나서 테스트하시기바랍니다.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfiguration.class)
class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customerDao.deleteAll();

        String name = "빙허";
        Money balance = Money.ZERO;
        customer = Customer.builder().name(name).balance(balance).build();
    }

    @DisplayName("add - 새로운 고객을 생성한다.")
    @Test
    void add_new_customer() {
        // given
        String name = "마크";
        Money balance = Money.ZERO;
        Customer customer = Customer.builder()
                .name(name)
                .balance(balance)
                .build();

        // when
        Long newCustomerId = customerDao.add(customer);

        // then
        Customer newCustomer = customerDao.findById(newCustomerId);
        assertThat(newCustomer.getId()).isEqualTo(newCustomerId);
        assertThat(newCustomer.getName()).isEqualTo(name);
        assertThat(newCustomer.getBalance().getValue()).isEqualTo(balance.getValue());
    }

    @DisplayName("update - 고객의 정보를 업데이트한다. (입금 정보)")
    @Test
    void update_customer() {
        // given
        Long customerId = customerDao.add(customer);
        Customer customer = customerDao.findById(customerId);

        // when
        customer.deposit(Money.of(BigDecimal.valueOf(100_000L)));
        customerDao.update(customer);

        // then
        Customer updatedCustomer = customerDao.findById(customerId);
        assertThat(updatedCustomer.getBalance().getValue()).isGreaterThanOrEqualTo(BigDecimal.valueOf(100_000L));
    }
}