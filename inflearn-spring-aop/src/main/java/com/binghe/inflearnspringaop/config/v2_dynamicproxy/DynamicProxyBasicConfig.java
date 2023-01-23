package com.binghe.inflearnspringaop.config.v2_dynamicproxy;

import com.binghe.inflearnspringaop.app.v1.OrderControllerV1;
import com.binghe.inflearnspringaop.app.v1.OrderControllerV1Impl;
import com.binghe.inflearnspringaop.app.v1.OrderRepositoryV1;
import com.binghe.inflearnspringaop.app.v1.OrderRepositoryV1Impl;
import com.binghe.inflearnspringaop.app.v1.OrderServiceV1;
import com.binghe.inflearnspringaop.app.v1.OrderServiceV1Impl;
import com.binghe.inflearnspringaop.config.v2_dynamicproxy.handler.LogTraceFilterInvocationHandler;
import com.binghe.inflearnspringaop.config.v2_dynamicproxy.handler.LogTraceInvocationHandler;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyBasicConfig {

    private static final String[] pattern = {"request*", "order*", "save*"};

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1Impl target = new OrderRepositoryV1Impl();
        LogTraceFilterInvocationHandler handler = new LogTraceFilterInvocationHandler(target, logTrace, pattern);
        return (OrderRepositoryV1) Proxy.newProxyInstance(
                OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                handler
        );
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1Impl target = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        LogTraceFilterInvocationHandler handler = new LogTraceFilterInvocationHandler(target, logTrace, pattern);
        return (OrderServiceV1) Proxy.newProxyInstance(
                OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                handler
        );
    }

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1Impl target = new OrderControllerV1Impl(orderServiceV1(logTrace));
        LogTraceFilterInvocationHandler handler = new LogTraceFilterInvocationHandler(target, logTrace, pattern);
        return (OrderControllerV1) Proxy.newProxyInstance(
                OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                handler
        );
    }
}
