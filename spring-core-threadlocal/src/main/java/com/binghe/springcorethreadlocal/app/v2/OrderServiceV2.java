package com.binghe.springcorethreadlocal.app.v2;

import com.binghe.springcorethreadlocal.trace.TraceId;
import com.binghe.springcorethreadlocal.trace.TraceStatus;
import com.binghe.springcorethreadlocal.trace.hellotrace.HelloTraceV1;
import com.binghe.springcorethreadlocal.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final HelloTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId) {
        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            orderRepository.save(status.getTraceId(), itemId);
            trace.end(status);
        } catch (IllegalStateException e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
