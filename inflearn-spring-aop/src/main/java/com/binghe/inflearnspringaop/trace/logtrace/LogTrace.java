package com.binghe.inflearnspringaop.trace.logtrace;


import com.binghe.inflearnspringaop.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}
