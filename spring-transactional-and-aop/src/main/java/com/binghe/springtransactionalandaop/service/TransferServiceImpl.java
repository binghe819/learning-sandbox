package com.binghe.springtransactionalandaop.service;

import com.binghe.springtransactionalandaop.domain.Customer;
import com.binghe.springtransactionalandaop.domain.Money;
import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class TransferServiceImpl implements TransferService {

    private final CustomerDao customerDao;

    public TransferServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void transfer(Long fromCustomerId, Long toCustomerId, Money amount) {
        // 비즈니스 로직
        Customer from = findById(fromCustomerId);
        Customer to = findById(toCustomerId);

        to.deposit(amount);
        customerDao.update(to);

        from.withdraw(amount);
        customerDao.update(from);
    }

    private Customer findById(Long id) {
        try {
            return customerDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("존재하지않는 Customer입니다.");
        }
    }
}
