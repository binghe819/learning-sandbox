package com.binghe.springbatchlearningtest.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExecutionContextConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job executionContextJob() {
        return jobBuilderFactory.get("executionContextJob")
                .start(executionContextStep1())
                .next(executionContextStep2())
                .next(executionContextStep3())
                .next(executionContextStep4())
                .build();
    }

    @Bean
    public Step executionContextStep1() {
        return stepBuilderFactory.get("executionContextStep1")
                .tasklet((contribution, chunkContext) -> {

                    System.out.println(" >>> executionContextStep1 실행됨");

                    ExecutionContext jobExecutionContext =
                            contribution.getStepExecution().getJobExecution().getExecutionContext();

                    ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                    String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
                    String stepName = chunkContext.getStepContext().getStepExecution().getStepName();

                    // Job과 Step ExecutionContext에 각각의 이름 넣기
                    if (!jobExecutionContext.containsKey("jobName")) {
                        jobExecutionContext.putString("jobName", jobName);
                    }
                    if (!stepExecutionContext.containsKey("stepName")) {
                        stepExecutionContext.putString("stepName", stepName);
                    }

                    System.out.println("Step 1 저장된 jobName : " + jobName);
                    System.out.println("Step 1 저장된 stepName : " + stepName);

                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step executionContextStep2() {
        return stepBuilderFactory.get("executionContextStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> executionContextStep2 실행됨");

                    // JobExecutionContext와 StepExecutionContext에서 값 가져와서 출력
                    ExecutionContext jobExecutionContext =
                            contribution.getStepExecution().getJobExecution().getExecutionContext();

                    ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                    String jobName = jobExecutionContext.getString("jobName");
                    Object stepName = stepExecutionContext.get("stepName");

                    System.out.println("Step 2 jobName 출력 : " + jobName); // executionContextJob 출력됨.
                    System.out.println("Step 2 stepName 출력 : " + stepName); // null이 출력됨.

                    String stepNameByContext = chunkContext.getStepContext().getStepExecution().getStepName();

                    if (!stepExecutionContext.containsKey("stepName")) {
                        stepExecutionContext.putString("stepName", stepNameByContext);
                    }

                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step executionContextStep3() {
        return stepBuilderFactory.get("executionContextStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> executionContextStep3 실행됨");

                    // 첫번째 실행에선 ExecutionContext에 name이 없기에 Job이 중단된다.
                    // 두번째 실행에선 Step1, Step2는 성공했기에 바로 Step3로 넘어오고, ExecutionContext에 name을 넣어놨기때문에 Job이 성공적으로 완수된다.
                    Object name = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("name");

                    if (name == null) {
                        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putString("name", "binghe");
                        throw new RuntimeException("Step3 예외 발생 - 다음 실행부터는 Step1, Step2는 건너뛰고 바로 Step3가 실행된다.");
                    }

                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step executionContextStep4() {
        return stepBuilderFactory.get("executionContextStep4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> executionContextStep4 실행됨");

                    Object name = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("name");
                    System.out.println("name : " + name);

                    return RepeatStatus.FINISHED;
                }).build();
    }
}
