package com.binghe.thrift;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorServiceImpl implements CalculatorService.Iface {

    private static final Logger log = LoggerFactory.getLogger(CalculatorServiceImpl.class);

    @Override
    public int add(int a, int b) throws org.apache.thrift.TException {
        int result = a + b;
        log.info("[server] com.binghe.thrift.CalculatorService.Iface.add() called. a: {}, b: {}, result: {}", a, b, result);
        return result;
    }

    @Override
    public int subtract(int a, int b) throws org.apache.thrift.TException {
        int result = a - b;
        log.info("[server] com.binghe.thrift.CalculatorService.Iface.subtract() called. a: {}, b: {}, result: {}", a, b, result);
        return result;
    }
}
