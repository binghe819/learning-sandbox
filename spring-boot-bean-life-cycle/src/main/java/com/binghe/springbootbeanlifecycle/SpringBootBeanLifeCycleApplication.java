package com.binghe.springbootbeanlifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootBeanLifeCycleApplication {

    @Bean(initMethod = "initByInitMethod", destroyMethod = "destroyByDestroyMethod")
    public ExampleBeanA exampleBeanA() {
        return new ExampleBeanA(exampleBeanB(), exampleBeanC());
    }

    @Bean(initMethod = "initByInitMethod", destroyMethod = "destroyByDestroyMethod")
    public ExampleBeanB exampleBeanB() {
        return new ExampleBeanB(exampleBeanC());
    }

    @Bean(initMethod = "initByInitMethod", destroyMethod = "destroyByDestroyMethod")
    public ExampleBeanC exampleBeanC() {
        return new ExampleBeanC();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBeanLifeCycleApplication.class, args);
    }

}
