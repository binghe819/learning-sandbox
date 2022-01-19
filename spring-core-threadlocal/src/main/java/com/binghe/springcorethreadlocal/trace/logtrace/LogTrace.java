package com.binghe.springcorethreadlocal.trace.logtrace;

import com.binghe.springcorethreadlocal.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
