package com.binghe.springbatchhellohelloworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SampleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job sampleJob() {
        return jobBuilderFactory.get("sampleJob")
                .start(sampleJobStepA(null))
                .next(sampleStepB(null))
                .build();
    }

    @Bean
    @JobScope
    public Step sampleJobStepA(@Value("#{jobParameters[date]}") String date) {
        return stepBuilderFactory.get("sampleJob")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("##### Step A - 실행됨.");
                    log.info("##### date = {}", date);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    @JobScope
    public Step sampleStepB(@Value("#{jobParameters[date]}") String date) {
        return stepBuilderFactory.get("sampleJob")
                .tasklet((contribution, chunkContext) -> {
                    log.info("##### Step B - 실행됨.");
                    log.info("##### date = {}", date);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
