package com.binghe.springbatchlearningtest.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * ChunkOrientedTasklet과 청크기반의 동작 순서를 디버깅하기 위한 Job
 */
@Configuration
@RequiredArgsConstructor
public class ChunkJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("chunkJob")
                .start(chunkJobStep1())
                .next(chunkJobStep2())
                .build();
    }

    @Bean
    public Step chunkJobStep1() {
        return stepBuilderFactory.get("chunkJobStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" >>> chunkJobStep1 실행됨");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step chunkJobStep2() {
        return stepBuilderFactory.get("chunkJobStep2")
                .<String, String>chunk(3)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        Thread.sleep(300);
                        System.out.println(item);
                        return "processed " + item;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        Thread.sleep(300);
                        System.out.println("Items : " + items);
                    }
                })
                .build();
    }
}
