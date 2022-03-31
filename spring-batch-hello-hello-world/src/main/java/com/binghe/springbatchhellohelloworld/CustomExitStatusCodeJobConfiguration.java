package com.binghe.springbatchhellohelloworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class CustomExitStatusCodeJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job customExitStatusJob() {
        return this.jobBuilderFactory.get("customExitStatusJob")
                .start(stepA())
                    .on("FAILED")
                    .end()
                .from(stepA())
                    .on("COMPLETED WITH SKIPS")
                    .to(printErrorStep())
                .from(stepA())
                    .on("*")
                    .to(stepB())
                    .end()
                .build();
    }

    @Bean
    public Step stepA() {
        return stepBuilderFactory.get("stepA")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step A");
                    contribution.incrementReadSkipCount();
                    contribution.incrementProcessSkipCount();
                    contribution.incrementWriteSkipCount();
//                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepB() {
        return stepBuilderFactory.get("stepB")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step B");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepC() {
        return stepBuilderFactory.get("stepC")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step C");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step printErrorStep() {
        return stepBuilderFactory.get("printErrorStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("##### Error!!!!!!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
