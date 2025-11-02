package com.binghe.example;

import io.neri.calculator.CalculatorService;
import io.neri.health.HealthService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 서버내 AsyncProcessor는 반드시 AsyncClient로 호출해야한다. (수정 필요)
 */
public class ThriftFramedTransportClientTest {

    private static final Logger log = LoggerFactory.getLogger(ThriftFramedTransportClientTest.class);

    public static void main(String[] args) {
        TTransport transport = null;
        try {
            // 서버 연결 (실제 서비스에선 Connection Pool 활용해야 함)
            TSocket socket = new TSocket("localhost", 8080);
            transport = new TFramedTransport(socket);
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
        } finally {
            if (transport != null && transport.isOpen()) {
                transport.close();
            }
        }

    }
}
