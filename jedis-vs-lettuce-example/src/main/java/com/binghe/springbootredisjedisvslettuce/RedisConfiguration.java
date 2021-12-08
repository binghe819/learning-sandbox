package com.binghe.springbootredisjedisvslettuce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    private final String host;
    private final String password;
    private final int port;

    public RedisConfiguration(
        @Value("${spring.redis.host}") String host,
        @Value("${spring.redis.password}") String password,
        @Value("${spring.redis.port}") int port
    ) {
        this.host = host;
        this.password = password;
        this.port = port;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration =
            new RedisStandaloneConfiguration(host, port);
        configuration.setPassword(password);

//        return new LettuceConnectionFactory(configuration);
        return new JedisConnectionFactory(configuration);
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration configuration =
//            new RedisStandaloneConfiguration(host, port);
//        configuration.setPassword(password);
//
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration);
//
//        GenericObjectPoolConfig<Jedis> poolConfig = jedisConnectionFactory
//            .getPoolConfig();
//        poolConfig.setMaxTotal(128);
//        poolConfig.setMaxIdle(128);
//        poolConfig.setMinIdle(36);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//        poolConfig.setTestWhileIdle(true);
//        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
//        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
//        poolConfig.setNumTestsPerEvictionRun(3);
//        poolConfig.setBlockWhenExhausted(true);
//
//        return jedisConnectionFactory;
//    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
