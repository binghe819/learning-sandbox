package com.binghe.springcorethreadlocal;

import com.binghe.springcorethreadlocal.trace.logtrace.LogTrace;
import com.binghe.springcorethreadlocal.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringCoreThreadlocalApplication {

    @Bean
    public LogTrace logTrace() {
        // return new FieldLogTrace();
        return new ThreadLocalLogTrace();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCoreThreadlocalApplication.class, args);
    }

}
