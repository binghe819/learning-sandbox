package com.binghe.springtransactionalandaop.service;

import com.binghe.springtransactionalandaop.domain.Customer;
import com.binghe.springtransactionalandaop.domain.Money;
import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class TransferService {

    private final CustomerDao customerDao;
    private final DataSource dataSource;

    public TransferService(CustomerDao customerDao, DataSource dataSource) {
        this.customerDao = customerDao;
        this.dataSource = dataSource;
    }

    public void transfer(Long fromCustomerId, Long toCustomerId, Money amount) throws SQLException {
        // 트랜잭션 동기화 관리자를 이용해 동기화 작업을 초기화한다.
        TransactionSynchronizationManager.initSynchronization();
        // DB 커넥션을 생성하고 트랜잭션을 시작한다.
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            con.setAutoCommit(false);

            // 비즈니스 로직
            Customer from = findById(fromCustomerId);
            Customer to = findById(toCustomerId);

            to.deposit(amount);
            customerDao.update(to);

            from.withdraw(amount);
            customerDao.update(from);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            // 스프링 유틸리티 메서드를 이용해 DB 커넥션을 안전하게 닫는다.
            DataSourceUtils.releaseConnection(con, dataSource);
            // 동기화 작업 종료 및 정리
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
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
