package com.binghe.springtransactionalandaop.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionInvocationHandler implements InvocationHandler {

    private final PlatformTransactionManager platformTransactionManager;
    private final Object target;
    private final String pattern;

    public TransactionInvocationHandler(PlatformTransactionManager platformTransactionManager, Object target, String pattern) {
        this.platformTransactionManager = platformTransactionManager;
        this.target = target;
        this.pattern = pattern;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith(pattern)) {
            return invokeInTransaction(method, args);
        }
        return method.invoke(target, args);
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = this.platformTransactionManager
                .getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = method.invoke(target, args);
            this.platformTransactionManager.commit(status);
            return ret;
        } catch (InvocationTargetException e) {
            this.platformTransactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}
