package com.binghe.springbatchlearningtest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 수동으로 JobLauncher를 통해 배치를 실행시키는 ApplicationRunner
 * -> 설정파일 (yml)에서 spring.batch.job.enabled = false로 해줘야한다.
 */
@Component
public class ManualJobLauncher implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final Collection<Job> jobs;

    public ManualJobLauncher(JobLauncher jobLauncher, Collection<Job> jobs) {
        this.jobLauncher = jobLauncher;
        this.jobs = jobs;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String jobName = "stepLearningTestJob"; // Job 이름

        Job job = determineJob(jobName);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "binghe")
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }

    private Job determineJob(String jobName) {
        return jobs.stream()
                .filter(job -> job.getName().equals(jobName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Job 이름입니다."));
    }
}
