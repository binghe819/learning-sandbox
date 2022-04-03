package com.binghe.springbatchhellohelloworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
@Configuration
public class JobScopeJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job scopeSampleJob() {
        return jobBuilderFactory.get("scopeSampleJob")
//                .start(systemParameterStep(null))
                .start(jobParametersStep(null))
                .build();
    }

//    @Bean
//    public Step systemParameterStep(@Value("${input.date}") String date) {
//        return stepBuilderFactory.get("systemParameterStep")
//                .tasklet(((contribution, chunkContext) -> {
//                    log.info("##### systemParameterStep - 실행됨.");
//                    log.info("##### date = {}", date);
//                    return RepeatStatus.FINISHED;
//                }))
//                .build();
//    }

    @Bean
    @JobScope
    public Step jobParametersStep(@Value("#{jobParameters[date]}") String date) {
        return stepBuilderFactory.get("jobParametersStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("##### jobParametersStep - 실행됨");
                    log.info("#### date = {}", date);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
