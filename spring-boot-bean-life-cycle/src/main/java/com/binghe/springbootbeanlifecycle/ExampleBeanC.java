package com.binghe.springbootbeanlifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ExampleBeanC implements InitializingBean, DisposableBean {

    public ExampleBeanC() {
        System.out.println("ExampleBeanC.생성자 Called");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("ExampleBeanC.postConstruct Called");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("ExampleBeanC.afterPropertiesSet Called");
    }

    public void initByInitMethod() {
        System.out.println("ExampleBeanC.initByInitMethod Called");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("ExampleBeanC.preDestory Called");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("ExampleBeanC.destroy Called");
    }

    public void destroyByDestroyMethod() {
        System.out.println("ExampleBeanC.destroyByDestroyMethod Called");
    }
}
