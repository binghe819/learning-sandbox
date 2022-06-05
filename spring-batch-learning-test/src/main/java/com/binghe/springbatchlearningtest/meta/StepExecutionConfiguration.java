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
public class StepExecutionConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepExecutionJob() {
        return jobBuilderFactory.get("stepExecutionJob")
                .start(stepExecutionStep1())
                .next(stepExecutionStep2())
                .next(stepExecutionStep3())
                .build();
    }

    @Bean
    public Step stepExecutionStep1() {
        return stepBuilderFactory.get("stepExecutionStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> stepExecutionStep1 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step stepExecutionStep2() {
        return stepBuilderFactory.get("stepExecutionStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> stepExecutionStep2 실행됨");

                    throw new RuntimeException("일부러 Step 실패");
//                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step stepExecutionStep3() {
        return stepBuilderFactory.get("stepExecutionStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> stepExecutionStep3 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
