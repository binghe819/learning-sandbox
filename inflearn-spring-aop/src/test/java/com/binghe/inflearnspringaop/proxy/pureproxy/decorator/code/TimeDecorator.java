package com.binghe.inflearnspringaop.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class TimeDecorator implements Component {

    private Component nextComponent;

    public TimeDecorator(Component nextComponent) {
        this.nextComponent = nextComponent;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = nextComponent.operation();
        long endTime = System.currentTimeMillis();
        log.info("TimeDecorator 종료 resultTime={}", endTime - startTime);
        return result;
    }
}
