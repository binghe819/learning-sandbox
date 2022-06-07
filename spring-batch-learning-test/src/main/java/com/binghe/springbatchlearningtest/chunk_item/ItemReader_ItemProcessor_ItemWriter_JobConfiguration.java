package com.binghe.springbatchlearningtest.chunk_item;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Chunk 기반의 처리 Hello World 겸 ItemReader, ItemProcessor, ItemWriter 디버깅용
 */
@Configuration
@RequiredArgsConstructor
public class ItemReader_ItemProcessor_ItemWriter_JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemJob() {
        return jobBuilderFactory.get("itemJob")
                .start(itemStep1())
                .next(itemStep2())
                .build();
    }

    @Bean
    @JobScope
    public Step itemStep1() {
        return stepBuilderFactory.get("itemStep1")
                .<User, String>chunk(2)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemReader<User> itemReader() {
        return new UserItemReader(Arrays.asList(
                new User("binghe"),
                new User("mark"),
                new User("byeonghwa"),
                new User("gogogo"),
                new User("batch")
        ));
    }

    @Bean
    public ItemProcessor<User, String> itemProcessor() {
        return new UserItemProcessor();
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return new UserItemWriter();
    }

    @Bean
    public Step itemStep2() {
        return stepBuilderFactory.get("itemStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("itemStep2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
