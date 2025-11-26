package com.binghe.tframedsocket_inflight_equal_1;

import io.netty.channel.Channel;
import org.apache.thrift.TConfiguration;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class TNettyFramedTransport extends TTransport {

    private final Channel channel;
    private final TConfiguration configuration;

    public TNettyFramedTransport(Channel channel) {
        this(channel, null);
    }

    public TNettyFramedTransport(Channel channel, TConfiguration configuration) {
        this.channel = channel;
        this.configuration = configuration != null ? configuration : new TConfiguration();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void open() throws TTransportException {

    }

    @Override
    public void close() {

    }

    @Override
    public int read(byte[] buf, int off, int len) throws TTransportException {
        return 0;
    }

    @Override
    public void write(byte[] buf, int off, int len) throws TTransportException {

    }

    @Override
    public TConfiguration getConfiguration() {
        return null;
    }

    @Override
    public void updateKnownMessageSize(long size) throws TTransportException {

    }

    @Override
    public void checkReadBytesAvailable(long numBytes) throws TTransportException {

    }
}
