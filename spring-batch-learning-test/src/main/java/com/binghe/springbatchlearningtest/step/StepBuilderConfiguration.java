package com.binghe.springbatchlearningtest.step;

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
public class StepBuilderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepBuilderJob() {
        return jobBuilderFactory.get("stepBuilderJob")
                .start(stepBuilderStep1())
                .next(stepBuilderStep2())
                .build();
    }

    @Bean
    public Step stepBuilderStep1() {
        return stepBuilderFactory.get("stepBuilderStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> stepBuilderStep1 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step stepBuilderStep2() {
        return stepBuilderFactory.get("stepBuilderStep2")
                .<String, String>chunk(3)
                .reader(() -> null)
                .writer(list -> {
                    System.out.println(" >>> stepBuilderStep2 Chunk 실행됨");
                })
                .build();
    }
}
