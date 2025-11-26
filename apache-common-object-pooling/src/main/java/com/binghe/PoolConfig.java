package com.binghe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PoolConfig {

    // Connection 설정
    private String host;
    private int port;

    // Pool 설정
    @Builder.Default
    private int maxTotal = 50;
    @Builder.Default
    private int maxIdle = 50;
    @Builder.Default
    private int minIdle = 2;
    @Builder.Default
    private long maxWaitMillis = 20;

    // TCP Connection Timeout 설정
    @Builder.Default
    private int socketTimeoutMillis = 2000;
    @Builder.Default
    private int connectionTimeoutMillis = 1000;

    public GenericObjectPoolConfig<TcpClientConnection> getPoolConfig() {
        GenericObjectPoolConfig<TcpClientConnection> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(true);
        // config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        return config;
    }
}
