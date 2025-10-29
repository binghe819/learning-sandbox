package com.binghe;

import io.netty.buffer.ByteBuf;
import org.apache.thrift.TConfiguration;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

// Thrift Netty Socket
public class TNettyChannelTransport extends TTransport {

    private final ByteBuf readBuffer_;
    private final ByteBuf writeBuffer_;
    private final TConfiguration configuration;

    public TNettyChannelTransport(ByteBuf readBuffer, ByteBuf writeBuffer) {
        this(readBuffer, writeBuffer, new TConfiguration());
    }

    public TNettyChannelTransport(ByteBuf readBuffer, ByteBuf writeBuffer, TConfiguration configuration) {
        this.readBuffer_ = readBuffer;
        this.writeBuffer_ = writeBuffer;
        this.configuration = configuration != null ? configuration : new TConfiguration();
    }

    @Override
    public boolean isOpen() {
        return readBuffer_ != null && readBuffer_.refCnt() > 0 
            && writeBuffer_ != null && writeBuffer_.refCnt() > 0;
    }

    @Override
    public void open() throws TTransportException {

    }

    @Override
    public void close() {

    }

    @Override
    public int read(byte[] buf, int off, int len) throws TTransportException {
        if (readBuffer_ == null || !readBuffer_.isReadable()) {
            return 0;
        }

        int bytesToRead = Math.min(readBuffer_.readableBytes(), len);
        readBuffer_.readBytes(buf, off, bytesToRead);
        return bytesToRead;
    }

    @Override
    public void write(byte[] buf, int off, int len) throws TTransportException {
        if (writeBuffer_ == null) {
            throw new TTransportException("Write buffer is null");
        }

        writeBuffer_.writeBytes(buf, off, len);
    }

    @Override
    public int readAll(byte[] buf, int off, int len) throws TTransportException {
        if (readBuffer_ == null) {
            throw new TTransportException("Read buffer is null");
        }

        int available = readBuffer_.readableBytes();
        if (available < len) {
            throw new TTransportException(
                    TTransportException.END_OF_FILE,
                    "Not enough bytes in buffer. Required: " + len + ", Available: " + available
            );
        }

        readBuffer_.readBytes(buf, off, len);
        return len;
    }

    @Override
    public void flush() throws TTransportException {
    }

    @Override
    public TConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void updateKnownMessageSize(long size) throws TTransportException {
        if (configuration != null) {
            configuration.setMaxMessageSize((int) size);
        }
    }

    @Override
    public void checkReadBytesAvailable(long numBytes) throws TTransportException {
        if (readBuffer_ == null) {
            throw new TTransportException("Read buffer is null");
        }

        int available = readBuffer_.readableBytes();
        if (available < numBytes) {
            throw new TTransportException(
                    TTransportException.END_OF_FILE,
                    "Not enough readable bytes. Required: " + numBytes + ", Available: " + available
            );
        }
    }
}
