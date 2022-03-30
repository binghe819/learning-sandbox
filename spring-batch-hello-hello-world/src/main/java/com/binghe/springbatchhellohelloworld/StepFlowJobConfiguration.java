package com.binghe.springbatchhellohelloworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
@Configuration
public class StepFlowJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepFlowJob() {
        return jobBuilderFactory.get("stepFlowJob")
                .start(stepFlowStepA())
                .next(stepFlowStepB())
                .next(stepFlowStepC())
                .build();
    }

    @Bean
    public Step stepFlowStepA() {
        return stepBuilderFactory.get("stepFlowJob")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("##### Step A - 실행됨.");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step stepFlowStepB() {
        return stepBuilderFactory.get("stepFlowJob")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("##### Step B - 실행됨.");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step stepFlowStepC() {
        return stepBuilderFactory.get("stepFlowJob")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("##### Step C - 실행됨.");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
