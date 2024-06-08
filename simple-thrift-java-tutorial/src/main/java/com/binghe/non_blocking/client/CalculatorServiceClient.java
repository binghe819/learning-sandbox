package com.binghe.non_blocking.client;

import com.binghe.thrift.CalculatorService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CalculatorServiceClient.class);

    public static void main(String[] args) {
        TTransport transport;
        try {
            transport = new TFramedTransport(new TSocket("localhost", 8080));
            TProtocol protocol = new TBinaryProtocol(transport);

            CalculatorService.Client client = new CalculatorService.Client(protocol);
            transport.open();

            long addResult = client.add(100, 50);
            log.info("[client] Add result: " + addResult);
            long substractResult = client.subtract(100, 50);
            log.info("[client] Substract result: " + substractResult);

            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
