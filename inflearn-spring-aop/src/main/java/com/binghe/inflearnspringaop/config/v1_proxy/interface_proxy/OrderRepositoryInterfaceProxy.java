package com.binghe.inflearnspringaop.config.v1_proxy.interface_proxy;

import com.binghe.inflearnspringaop.app.v1.OrderRepositoryV1;
import com.binghe.inflearnspringaop.trace.TraceStatus;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;

public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private OrderRepositoryV1 target;
    private LogTrace logTrace;

    public OrderRepositoryInterfaceProxy(OrderRepositoryV1 target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void save(String itemId) {

        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderRepository.save()");
            // target 호출
            target.save(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
