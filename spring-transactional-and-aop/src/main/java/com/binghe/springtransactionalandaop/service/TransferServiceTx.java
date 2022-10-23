package com.binghe.springtransactionalandaop.service;

import com.binghe.springtransactionalandaop.domain.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class TransferServiceTx implements TransferService {

    private final TransferService target;
    private final PlatformTransactionManager platformTransactionManager;

    public TransferServiceTx(TransferService target, PlatformTransactionManager platformTransactionManager) {
        this.target = target;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Override
    public void transfer(Long fromCustomerId, Long toCustomerId, Money amount) {
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            target.transfer(fromCustomerId, toCustomerId, amount);
            platformTransactionManager.commit(status);
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
            throw e;
        }
    }
}
