package com.binghe.inflearnspringaop.proxy.pureproxy.decorator;

import com.binghe.inflearnspringaop.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import com.binghe.inflearnspringaop.proxy.pureproxy.decorator.code.MessageDecorator;
import com.binghe.inflearnspringaop.proxy.pureproxy.decorator.code.RealComponent;
import com.binghe.inflearnspringaop.proxy.pureproxy.decorator.code.TimeDecorator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noProxyTest() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

        client.execute();
    }

    @Test
    void decoratorProxyTest() {
        RealComponent realComponent = new RealComponent();
        MessageDecorator decorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(decorator);

        client.execute();
    }

    @Test
    void twoDecoratorProxyTest() {
        RealComponent realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);

        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);

        client.execute();
    }
}
