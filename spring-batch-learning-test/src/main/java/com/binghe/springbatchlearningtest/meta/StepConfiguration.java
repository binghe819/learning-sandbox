package com.binghe.springbatchlearningtest.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tasklet이 실행되는 과정을 디버깅하기 좋은 Job Configuration
 */
@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepLearningTestJob() {
        return jobBuilderFactory.get("stepLearningTestJob")
                .start(stepLearningTestStep1())
                .next(stepLearningTestStep2())
                .build();
    }

    @Bean
    public Step stepLearningTestStep1() {
        return stepBuilderFactory.get("stepLearningTestStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> stepLearningTestStep1 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step stepLearningTestStep2() {
        return stepBuilderFactory.get("stepLearningTestStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> stepLearningTestStep2 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
