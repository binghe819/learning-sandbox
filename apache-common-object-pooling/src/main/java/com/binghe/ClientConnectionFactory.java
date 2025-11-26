package com.binghe;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ClientConnectionFactory extends BasePooledObjectFactory<TcpClientConnection> {

    private final PoolConfig poolConfig;

    public ClientConnectionFactory(PoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    @Override
    public TcpClientConnection create() throws Exception {
        return new TcpClientConnection(
                poolConfig.getHost(),
                poolConfig.getPort(),
                poolConfig.getConnectionTimeoutMillis(),
                poolConfig.getSocketTimeoutMillis()
        );
    }

    @Override
    public PooledObject<TcpClientConnection> wrap(TcpClientConnection tcpClientConnection) {
        return new DefaultPooledObject<>(tcpClientConnection);
    }

    @Override
    public void destroyObject(PooledObject<TcpClientConnection> p) throws Exception {
        TcpClientConnection connection = p.getObject();
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<TcpClientConnection> p) {
        TcpClientConnection connection = p.getObject();
        return connection != null && connection.isValid();
    }
}
