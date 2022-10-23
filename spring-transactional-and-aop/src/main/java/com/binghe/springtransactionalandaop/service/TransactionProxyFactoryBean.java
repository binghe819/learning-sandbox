package com.binghe.springtransactionalandaop.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TransactionProxyFactoryBean implements FactoryBean<Object> {
    private final Object target;
    private final PlatformTransactionManager platformTransactionManager;
    private final String pattern;
    private final Class<?> serviceInterface;

    public TransactionProxyFactoryBean(Object target,
                                       PlatformTransactionManager platformTransactionManager,
                                       String pattern,
                                       Class<?> serviceInterface) {
        this.target = target;
        this.platformTransactionManager = platformTransactionManager;
        this.pattern = pattern;
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionInvocationHandler txHandler = new TransactionInvocationHandler(platformTransactionManager, target, pattern);
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { serviceInterface },
                txHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
