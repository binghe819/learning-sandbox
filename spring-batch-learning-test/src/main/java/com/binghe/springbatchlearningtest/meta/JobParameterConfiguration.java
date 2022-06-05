package com.binghe.springbatchlearningtest.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobParameterJob() {
        return jobBuilderFactory.get("jobParameterJob")
                .start(jobParameterJobStep1())
                .next(jobParameterJobStep2())
                .build();
    }

    @Bean
    public Step jobParameterJobStep1() {
        return stepBuilderFactory.get("jobParameterJobStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> jobParameterJobStep1 실행됨");

                    // JobParameters를 가져오는 방법 1
                    JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                    String name = jobParameters.getString("name");
                    System.out.println(name + "에 의해서 실행됨");

                    // JobParameters를 가져오는 방법 2 - 조회용 방식 (해당 내용을 변경해도 변경되지 않는다)
                    Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();
                    String name2 = (String) jobParameters2.get("name");
                    System.out.println(name2 + " 확인");

                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step jobParameterJobStep2() {
        return stepBuilderFactory.get("jobParameterJobStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> jobParameterJobStep2 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
