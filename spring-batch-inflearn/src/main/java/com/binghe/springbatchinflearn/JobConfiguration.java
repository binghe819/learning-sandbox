package com.binghe.springbatchinflearn;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .build();
    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloJob")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hello Sptring Batch");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
