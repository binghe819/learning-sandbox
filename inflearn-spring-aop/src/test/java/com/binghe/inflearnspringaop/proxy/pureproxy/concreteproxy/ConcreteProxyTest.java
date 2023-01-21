package com.binghe.inflearnspringaop.proxy.pureproxy.concreteproxy;

import com.binghe.inflearnspringaop.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import com.binghe.inflearnspringaop.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import com.binghe.inflearnspringaop.proxy.pureproxy.concreteproxy.code.TimeProxy;
import org.junit.jupiter.api.Test;

public class ConcreteProxyTest {

    @Test
    void noProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient concreteClient = new ConcreteClient(concreteLogic);

        concreteClient.execute();
    }

    @Test
    void timeProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteLogic timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient client = new ConcreteClient(timeProxy);

        client.execute();
    }
}
