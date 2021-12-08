package com.binghe.springbootbeanlifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ExampleBeanB implements InitializingBean, DisposableBean {

    private final ExampleBeanC exampleBeanC;

    public ExampleBeanB(ExampleBeanC exampleBeanC) {
        this.exampleBeanC = exampleBeanC;
        System.out.println("ExampleBeanB.생성자 Called");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("ExampleBeanB.postConstruct Called");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("ExampleBeanB.afterPropertiesSet Called");
    }

    public void initByInitMethod() {
        System.out.println("ExampleBeanB.initByInitMethod Called");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("ExampleBeanB.preDestory Called");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("ExampleBeanB.destroy Called");
    }

    public void destroyByDestroyMethod() {
        System.out.println("ExampleBeanB.destroyByDestroyMethod Called");
    }
}
