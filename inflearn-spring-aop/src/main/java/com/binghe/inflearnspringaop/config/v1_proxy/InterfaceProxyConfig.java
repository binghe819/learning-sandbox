package com.binghe.inflearnspringaop.config.v1_proxy;

import com.binghe.inflearnspringaop.app.v1.OrderControllerV1;
import com.binghe.inflearnspringaop.app.v1.OrderControllerV1Impl;
import com.binghe.inflearnspringaop.app.v1.OrderRepositoryV1;
import com.binghe.inflearnspringaop.app.v1.OrderRepositoryV1Impl;
import com.binghe.inflearnspringaop.app.v1.OrderServiceV1;
import com.binghe.inflearnspringaop.app.v1.OrderServiceV1Impl;
import com.binghe.inflearnspringaop.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import com.binghe.inflearnspringaop.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import com.binghe.inflearnspringaop.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;
import com.binghe.inflearnspringaop.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1Impl target = new OrderControllerV1Impl(orderService(logTrace));
        return new OrderControllerInterfaceProxy(target, logTrace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl target = new OrderServiceV1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(target, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1Impl target = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(target, logTrace);
    }
}
