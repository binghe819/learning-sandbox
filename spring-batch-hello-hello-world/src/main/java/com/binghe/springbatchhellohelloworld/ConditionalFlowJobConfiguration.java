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
public class ConditionalFlowJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepFlowConditionalJob() {
        return jobBuilderFactory.get("conditionalFlowJob")
                .start(conditionalJobStepA()) // A에서 출발해서
                    .on("*") // A의 결과에 관계없이
                    .to(conditionalJobStepB()) // B로 간다.
                    .on("*")
                    .to(conditionalJobStepC())
                .from(conditionalJobStepA()) // A에서 출발해서
                    .on("FAILED") // A가 실패하면
                    .to(conditionalJobStepC()) // C로 간다.
                    .end() // C의 처리가 완료되면 종료된다.
                .build();
        //        return jobBuilderFactory.get("conditionalFlowJob")
//                .start(conditionalJobStepA()) // A에서 출발해서
//                    .on("FAILED") // A가 실패하면
//                    .to(conditionalJobStepC()) // C로 간다
//                    .on("*") // C의 결과에 관계없이
//                    .end() // C의 처리가 완료되면 종료한다
//                .from(conditionalJobStepA()) // A에서 출발해서
//                    .on("*") // 결과에 관계없이
//                    .to(conditionalJobStepB()) // B로 간다
//                    .on("*") // B의 결과에 관계없이
//                    .to(conditionalJobStepC()) // C로 간다.
//                    .end() // C의 처리가 완료되면 종료한다
//                .build();
    }

    @Bean
    public Step conditionalJobStepA() {
        return stepBuilderFactory.get("stepA")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step A");

//                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStepB() {
        return stepBuilderFactory.get("conditionalJobStepB")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step B");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStepC() {
        return stepBuilderFactory.get("conditionalJobStepC")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step C");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
