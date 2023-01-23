package com.binghe.inflearnspringaop.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @DisplayName("리플렉션 사용전 - 공통 로직 1과 공통 로직 2를 하나의 메서드로 공통화하기 어렵다.")
    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직 1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드만 다름.
        log.info("result={}", result1);
        // 공통 로직 2 종료

        // 공통 로직 2 시작
        log.info("start");
        String result2 = target.callB();
        log.info("result={}", result2);
        // 공통 로직 2 종료
    }

    /**
     * 리플렉션은 클래스나 메서드의 메타정보를 사용해서 동적으로 호출하는 메서드를 변경할 수 있다.
     */
    @DisplayName("리플렉션 사용후")
    @Test
    void reflection1() throws Exception {
        // 클래스정보
        Class classHello = Class.forName("com.binghe.inflearnspringaop.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        // callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}", result1);

        // callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
    }

    /**
     * 정적인 target.callA() , target.callB() 코드를 리플렉션을 사용해서 Method 라는 메타정보로 추상화했다. 덕분에 공통 로직을 만들 수 있게 되었다.
     *
     */
    @DisplayName("리플렉션 사용하여 메서드 분리 (공통화함)")
    @Test
    void reflection2() throws Exception {
        // 클래스 정보
        Class classHello = Class.forName("com.binghe.inflearnspringaop.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello hello = new Hello();

        dynamicCall(classHello.getMethod("callA"), hello);
        dynamicCall(classHello.getMethod("callB"), hello);
    }

    private void dynamicCall(Method method, Hello target) throws InvocationTargetException, IllegalAccessException {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }
        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
