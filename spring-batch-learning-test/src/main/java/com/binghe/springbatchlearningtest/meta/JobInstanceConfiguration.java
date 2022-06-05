package com.binghe.springbatchlearningtest.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobInstanceConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobInstanceJob() {
        return jobBuilderFactory.get("jobInstanceJob")
                .start(jobInstanceJobStep1(null))
                .next(jobInstanceJobStep2())
                .build();
    }

    @Bean
    @JobScope
    public Step jobInstanceJobStep1(@Value("#{jobParameters[name]}") String name) {
        return stepBuilderFactory.get("jobInstanceJobStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> jobInstanceJobStep1 실행됨");
                    System.out.println(name + "에 의해 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step jobInstanceJobStep2() {
        return stepBuilderFactory.get("jobInstanceJobStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> jobInstanceJobStep2 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
