package com.binghe.inflearnspringaop.config.v3_proxyfactory;

import com.binghe.inflearnspringaop.app.v1.OrderControllerV1;
import com.binghe.inflearnspringaop.app.v1.OrderControllerV1Impl;
import com.binghe.inflearnspringaop.app.v1.OrderRepositoryV1;
import com.binghe.inflearnspringaop.app.v1.OrderRepositoryV1Impl;
import com.binghe.inflearnspringaop.app.v1.OrderServiceV1;
import com.binghe.inflearnspringaop.app.v1.OrderServiceV1Impl;
import com.binghe.inflearnspringaop.app.v2.OrderControllerV2;
import com.binghe.inflearnspringaop.app.v2.OrderRepositoryV2;
import com.binghe.inflearnspringaop.app.v2.OrderServiceV2;
import com.binghe.inflearnspringaop.config.v3_proxyfactory.advice.LogTraceAdvice;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
        OrderRepositoryV2 target = new OrderRepositoryV2();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        log.info("ProxyFactory proxy={}, target={}", proxyFactory.getProxy(), target.getClass());
        return (OrderRepositoryV2) proxyFactory.getProxy();
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
        OrderServiceV2 target = new OrderServiceV2(orderRepositoryV2(logTrace));
        ProxyFactory factory = new ProxyFactory(target);
        factory.addAdvisor(getAdvisor(logTrace));
        return (OrderServiceV2) factory.getProxy();
    }

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace logTrace) {
        OrderControllerV2 target = new OrderControllerV2(orderServiceV2(logTrace));
        ProxyFactory factory = new ProxyFactory(target);
        factory.addAdvisor(getAdvisor(logTrace));
        return (OrderControllerV2) factory.getProxy();
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
