package com.binghe.springbootbeanlifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ExampleBeanA implements InitializingBean, DisposableBean {

    private final ExampleBeanB exampleBeanB;

    private final ExampleBeanC exampleBeanC;

    public ExampleBeanA(
        ExampleBeanB exampleBeanB,
        ExampleBeanC exampleBeanC
    ) {
        System.out.println("ExampleBeanA.생성자 Called");
        this.exampleBeanB = exampleBeanB;
        this.exampleBeanC = exampleBeanC;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("ExampleBeanA.postConstruct Called");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("ExampleBeanA.afterPropertiesSet Called");
    }

    public void initByInitMethod() {
        System.out.println("ExampleBeanA.initByInitMethod Called");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("ExampleBeanA.preDestory Called");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("ExampleBeanA.destroy Called");
    }

    public void destroyByDestroyMethod() {
        System.out.println("ExampleBeanA.destroyByDestroyMethod Called");
    }
}
