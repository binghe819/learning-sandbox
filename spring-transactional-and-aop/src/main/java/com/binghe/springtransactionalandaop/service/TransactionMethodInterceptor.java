package com.binghe.springtransactionalandaop.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TransactionAdvice라고보면된다.
 */
public class TransactionMethodInterceptor implements MethodInterceptor {

    private PlatformTransactionManager platformTransactionManager;

    public TransactionMethodInterceptor(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 콜백을 호출해서 타깃의 메서드를 실행한다.
            Object ret = invocation.proceed();// target 호출
            this.platformTransactionManager.commit(status);
            return ret;
        } catch (RuntimeException e) {
            this.platformTransactionManager.rollback(status);
            throw e;
        }
    }

    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }
}
