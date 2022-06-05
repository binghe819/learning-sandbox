package com.binghe.springbatchlearningtest.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobExecutionConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobExecutionJob() {
        return jobBuilderFactory.get("jobExecutionJob")
                .start(jobExecutionStep1())
                .next(jobExecutionJobStep2())
                .build();
    }

    @Bean
    public Step jobExecutionStep1() {
        return stepBuilderFactory.get("jobExecutionStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> jobExecutionJobStep1 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step jobExecutionJobStep2() {
        return stepBuilderFactory.get("jobExecutionJobStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> jobExecutionJobStep2 실행됨");

                    throw new RuntimeException("일부러 예외 발생시켜서 Job 실패시키기");

//                    return RepeatStatus.FINISHED;
                }).build();
    }
}
