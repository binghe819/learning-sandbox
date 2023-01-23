package com.binghe.inflearnspringaop;

import com.binghe.inflearnspringaop.config.AppV1Config;
import com.binghe.inflearnspringaop.config.AppV2Config;
import com.binghe.inflearnspringaop.config.v1_proxy.ConcreteProxyConfig;
import com.binghe.inflearnspringaop.config.v1_proxy.InterfaceProxyConfig;
import com.binghe.inflearnspringaop.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import com.binghe.inflearnspringaop.config.v3_proxyfactory.ProxyFactoryConfigV1;
import com.binghe.inflearnspringaop.config.v3_proxyfactory.ProxyFactoryConfigV2;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;
import com.binghe.inflearnspringaop.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import({AppV1Config.class, AppV2Config.class}) // 클래스를 스프링빈으로 등록할 때 사용된다.
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
@Import(ProxyFactoryConfigV1.class)
//@Import(ProxyFactoryConfigV2.class)
@SpringBootApplication(scanBasePackages = "com.binghe.inflearnspringaop.app")
public class InflearnSpringAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(InflearnSpringAopApplication.class, args);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
