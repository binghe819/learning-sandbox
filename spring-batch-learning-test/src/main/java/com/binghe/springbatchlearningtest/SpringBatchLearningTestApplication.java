package com.binghe.springbatchlearningtest;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchLearningTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchLearningTestApplication.class, args);
    }

}
