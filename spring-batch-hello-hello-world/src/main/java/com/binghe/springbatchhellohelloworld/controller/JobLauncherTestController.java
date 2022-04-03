//package com.binghe.springbatchhellohelloworld.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//public class JobLauncherTestController {
//
//    private final JobLauncher jobLauncher;
//    private final Job scopeSampleJob;
//
//    @GetMapping("/batch")
//    public ResponseEntity<String> launchBatch(@RequestParam("date") String date) {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("date", date)
//                .toJobParameters();
//
//        try {
//            jobLauncher.run(scopeSampleJob, jobParameters);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.ok("error");
//        }
//
//        return ResponseEntity.ok("ok");
//    }
//}
