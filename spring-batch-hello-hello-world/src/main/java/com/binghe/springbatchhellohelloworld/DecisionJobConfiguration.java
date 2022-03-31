package com.binghe.springbatchhellohelloworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
@Configuration
public class DecisionJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("decisionJob")
                .start(decisionJobStepA())
                .next(decider()) // customDecider로 구분
                .from(decider()) // Decider 상태가
                    .on("TRUE") // TRUE라면
                    .to(decisionJobStepB()) // StepB로 간다
                .from(decider()) // Decider 상태가
                    .on("FALSE") // FALSE라면
                    .to(decisionJobStepC()) // StepC로 간다.
                .end()
                .build();
    }

    @Bean
    public Step decisionJobStepA() {
        return stepBuilderFactory.get("stepA")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step A");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step decisionJobStepB() {
        return stepBuilderFactory.get("stepB")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step B");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step decisionJobStepC() {
        return stepBuilderFactory.get("stepC")
                .tasklet((contribution, chunkContext) -> {
                    log.info("###### StepFlowConditionalJob Step C");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new CustomDecider();
    }

    static class CustomDecider implements JobExecutionDecider {
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            if (someCondition()) {
                return new FlowExecutionStatus("TRUE");
            }
            return new FlowExecutionStatus("FALSE");
        }

        private boolean someCondition() {
            return false;
        }
    }
}
