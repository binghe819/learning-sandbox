package com.binghe.springbatchlearningtest.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
 * ChunkProvider와 ChunkProcessor 디버깅시 사용할 Job
 */
@Configuration
@RequiredArgsConstructor
public class ChunkProviderChunkProcessorJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkProviderChunkProcessorJob() {
        return jobBuilderFactory.get("chunkProviderChunkProcessorJob")
                .start(chunkProviderChunkProcessorStep1())
                .next(chunkProviderChunkProcessorStep2())
                .build();
    }

    @Bean
    @JobScope
    public Step chunkProviderChunkProcessorStep1() {
        return stepBuilderFactory.get("chunkProviderChunkProcessorStep1")
                .<String, String>chunk(2)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3","item4", "item5", "item6")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        return "processed " + item;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        items.forEach(System.out::println);
                    }
                })
                .build();
    }

    @Bean
    public Step chunkProviderChunkProcessorStep2() {
        return stepBuilderFactory.get("chunkProviderChunkProcessorStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("chunkProviderChunkProcessorStep2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
