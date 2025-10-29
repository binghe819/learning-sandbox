package com.binghe.processor;

import io.neri.health.HealthService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthCheckServiceImpl implements HealthService.Iface {

    private final Logger log = LoggerFactory.getLogger(HealthCheckServiceImpl.class);

    @Override
    public String healthCheck() throws TException {
        log.info("healthCheck() called");
        return "OK";
    }
}
