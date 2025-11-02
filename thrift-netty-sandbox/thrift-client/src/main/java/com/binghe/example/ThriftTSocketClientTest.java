package com.binghe.example;

import io.neri.calculator.CalculatorService;
import io.neri.health.HealthService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftTSocketClientTest {

    private static final Logger log = LoggerFactory.getLogger(ThriftTSocketClientTest.class);

    public static void main(String[] args) {
        TTransport transport;
        try {
            // 서버 연결 (실제 서비스에선 Connection Pool 활용해야 함)
            transport = new TSocket("localhost", 8080);
            transport.open();

            // 기본 프로토콜 설정 (직렬화/역직렬화 프로토콜 -> Binary/JSON/...)
            TProtocol protocol = new TBinaryProtocol(transport);

            // Multiplexed Protocol 생성 (서비스 이름에 주의!)
            TMultiplexedProtocol calculatorProtocol = new TMultiplexedProtocol(protocol, "CalculatorService");
            TMultiplexedProtocol healthProtocol = new TMultiplexedProtocol(protocol, "HealthService");

            // Health 호출
            HealthService.Client healthCheckClient = new HealthService.Client(healthProtocol);
            log.info("[healthCheck] "+ healthCheckClient.healthCheck());
            try {
                healthCheckClient.throwException();
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            // Calculator 호출
            CalculatorService.Client calculatorService = new CalculatorService.Client(calculatorProtocol);

            long addResult = calculatorService.add(100, 50);
            log.info("[calculatorService] Add result: " + addResult);
            long substractResult = calculatorService.subtract(100, 50);
            log.info("[calculatorService] Substract result: " + substractResult);

            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
