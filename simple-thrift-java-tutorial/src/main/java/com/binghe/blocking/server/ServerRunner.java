package com.binghe.blocking.server;

import com.binghe.thrift.CalculatorService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRunner {

    private static final Logger log = LoggerFactory.getLogger(ServerRunner.class);

    public static void main(String[] args) {
        try {
            // Transport
            TServerSocket serverTransport = new TServerSocket(8080);

            // Processor
            CalculatorService.Processor processor = new CalculatorService.Processor(new CalculatorServiceImpl());

            // Server
            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport)
                            .processor(processor)
            );

            log.info("Starting server on port : 8080");

            server.serve();
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }
}
