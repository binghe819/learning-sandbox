package com.binghe.threadperrequestmodelperformanceimprovesynchronous.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfiguration {

    @Bean
    public ExecutorService asyncExecutorService() {
        return Executors.newCachedThreadPool();
    }
}
