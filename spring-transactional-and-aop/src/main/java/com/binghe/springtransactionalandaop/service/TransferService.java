package com.binghe.springtransactionalandaop.service;

import com.binghe.springtransactionalandaop.domain.Customer;
import com.binghe.springtransactionalandaop.domain.Money;
import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class TransferService {

    private final CustomerDao customerDao;
    private final PlatformTransactionManager platformTransactionManager;

    public TransferService(CustomerDao customerDao, PlatformTransactionManager platformTransactionManager) {
        this.customerDao = customerDao;
        this.platformTransactionManager = platformTransactionManager;
    }

    public void transfer(Long fromCustomerId, Long toCustomerId, Money amount) {
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // 비즈니스 로직
            Customer from = findById(fromCustomerId);
            Customer to = findById(toCustomerId);

            to.deposit(amount);
            customerDao.update(to);

            from.withdraw(amount);
            customerDao.update(from);
            platformTransactionManager.commit(status);
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
            throw e;
        }
    }

    private Customer findById(Long id) {
        try {
            return customerDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("존재하지않는 Customer입니다.");
        }
    }
}
