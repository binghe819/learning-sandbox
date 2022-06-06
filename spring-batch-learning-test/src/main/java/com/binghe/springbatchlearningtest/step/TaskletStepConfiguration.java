package com.binghe.springbatchlearningtest.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class TaskletStepConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job taskletStepJob() {
        return jobBuilderFactory.get("taskletStepJob")
                .start(taskletStepStep1())
                .next(taskletStepStep2())
                .build();
    }

    @Bean
    public Step taskletStepStep1() {
        return stepBuilderFactory.get("taskletStepStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> taskletStepStep1 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step taskletStepStep2() {
        return stepBuilderFactory.get("taskletStepStep2")
                .<String, String>chunk(3)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
                .processor((ItemProcessor<String, String>) String::toUpperCase)
                .writer(items -> items.forEach(System.out::println))
                .build();
    }
}
