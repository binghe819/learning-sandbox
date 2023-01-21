package com.binghe.inflearnspringaop.config.v1_proxy.interface_proxy;

import com.binghe.inflearnspringaop.app.v1.OrderControllerV1;
import com.binghe.inflearnspringaop.trace.TraceStatus;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;

public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final OrderControllerV1 target;
    private final LogTrace logTrace;

    public OrderControllerInterfaceProxy(OrderControllerV1 target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderController.request()");
            // target 호출
            target.request(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        return "ok";
    }

    @Override
    public String nolog() {
        return target.nolog();
    }
}
