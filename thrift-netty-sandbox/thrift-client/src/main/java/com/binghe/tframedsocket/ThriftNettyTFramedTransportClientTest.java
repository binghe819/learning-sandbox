package com.binghe.tframedsocket_inflight_equal_1;

import io.neri.calculator.CalculatorService;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class ThriftNettyTFramedTransportClientTest {

    public static void main(String[] args) {

        Channel channel = null;
        try {
            EventLoopGroup eventLoopGroup = new MultiThreadIoEventLoopGroup(
                    10,
                    new DefaultThreadFactory("neri-netty-", true),
                    NioIoHandler.newFactory());

            ThriftNettyTFramedTransportConnectionManager connectionManager = new ThriftNettyTFramedTransportConnectionManager(eventLoopGroup, 8080, "127.0.0.1");

            channel = connectionManager.createNewConnection();
            TProtocol protocol = new TBinaryProtocol(new TNettyFramedTransport(channel));

            CalculatorService.Client client = new CalculatorService.Client(protocol);
            System.out.println(client.add(10, 20));

            channel.close();
        } catch (Exception e) {

        } finally {
            if (channel != null) {
                channel.close();
            }
        }
    }
}
