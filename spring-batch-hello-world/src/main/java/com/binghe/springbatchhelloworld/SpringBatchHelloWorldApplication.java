package com.binghe.springbatchhelloworld;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchHelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchHelloWorldApplication.class, args);
    }

}
