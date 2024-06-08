package com.binghe.non_blocking.server;

import com.binghe.thrift.CalculatorService;
import com.binghe.thrift.CalculatorServiceImpl;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRunner {

    private static final Logger log = LoggerFactory.getLogger(ServerRunner.class);

    public static void main(String[] args) {
        try {
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(8080);

            CalculatorService.Processor processor = new CalculatorService.Processor(new CalculatorServiceImpl());

            TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).
                    processor(processor));

            log.info("[server] Starting server on port : 8080");

            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
