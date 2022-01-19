package com.binghe.springcorethreadlocal.app.v2;

import com.binghe.springcorethreadlocal.trace.TraceId;
import com.binghe.springcorethreadlocal.trace.TraceStatus;
import com.binghe.springcorethreadlocal.trace.hellotrace.HelloTraceV1;
import com.binghe.springcorethreadlocal.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final HelloTraceV2 trace;

    public void save(TraceId traceId, String itemId) {
        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId, "OrderRepositove.save()");

            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1_000);

            trace.end(status);
        } catch (IllegalStateException e) {
            trace.exception(status, e);
            throw e;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
