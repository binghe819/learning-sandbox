package com.binghe.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * readiness 판정에 "내가 원하는 기준"을 명시적으로 편입시키기 위한 커스텀 HealthIndicator.
 *
 * <p>Spring 기본 동작({@code readinessState} enum 자동 판단)만으로는 부족하고,
 * 애플리케이션이 스스로 정의한 조건(여기서는 <b>웜업 완료 여부</b>)으로 트래픽 수신 준비를
 * 판정하고 싶을 때 사용한다.
 *
 * <p>동작 연결:
 * <ul>
 *   <li>{@code application.yml}의 {@code management.endpoint.health.group.readiness.include}에
 *       이 인디케이터의 이름({@code myReadiness})을 등록한다.</li>
 *   <li>그 순간부터 {@code /actuator/health/readiness}는 {@code readinessState}와 이 인디케이터를
 *       <b>AND로 집계</b>한다. 즉 <b>둘 다 UP이어야 200(Ready)</b>이고, 하나라도 아니면 503이다.</li>
 * </ul>
 *
 * <p>상태 전환은 {@link com.binghe.warmup.WarmupRunner}가 웜업을 마친 뒤 {@link #markReady()}를
 * 호출하는 것으로 이뤄진다. 그 전까지는 {@code OUT_OF_SERVICE}(503)를 반환해 트래픽을 막는다.
 *
 * <p>bean 이름을 {@code "myReadiness"}로 명시한 이유: Actuator는 이 이름을 그대로 health
 * component 키로 쓰고, yaml의 {@code include}에서도 같은 이름으로 참조하기 때문이다.
 */
@Component("myReadiness")
public class MyReadinessHealthIndicator implements HealthIndicator {

    // 여러 스레드(웜업 스레드 / probe 처리 스레드)가 동시에 접근하므로 원자적 플래그를 쓴다.
    private final AtomicBoolean ready = new AtomicBoolean(false);

    /** 웜업 등 준비 작업이 끝났을 때 호출한다. 이후 readiness가 UP(200)이 된다. */
    public void markReady() {
        ready.set(true);
    }

    /** 필요 시 다시 트래픽을 거부하고 싶을 때 호출한다(예: 런타임 중 의존성 이상 감지). */
    public void markNotReady() {
        ready.set(false);
    }

    @Override
    public Health health() {
        // 판정 기준을 이 자리에 자유롭게 확장할 수 있다.
        //   예) 캐시 적재 여부, 특정 외부 의존성 ping, 기능 플래그 등을 조합해 UP/DOWN 결정.
        //   현재는 "웜업 완료 플래그" 하나만 기준으로 삼는다.
        return ready.get()
                ? Health.up().withDetail("reason", "warmed-up").build()
                : Health.outOfService().withDetail("reason", "warming-up").build();
    }
}
