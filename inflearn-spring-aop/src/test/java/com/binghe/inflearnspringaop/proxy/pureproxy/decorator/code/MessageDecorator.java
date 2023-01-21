package com.binghe.inflearnspringaop.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component {

    private Component target;

    public MessageDecorator(Component target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("Message Decorator 실행");
        String targetResult = target.operation();
        String decoResult = "*****" + targetResult + "*****";
        return decoResult;
    }
}
