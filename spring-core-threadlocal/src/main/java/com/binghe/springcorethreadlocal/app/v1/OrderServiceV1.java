package com.binghe.springcorethreadlocal.app.v1;

import com.binghe.springcorethreadlocal.trace.TraceStatus;
import com.binghe.springcorethreadlocal.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        } catch (IllegalStateException e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
