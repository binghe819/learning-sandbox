package com.binghe.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * liveness 판정에 "내가 원하는 기준"을 명시적으로 편입시키기 위한 커스텀 HealthIndicator.
 *
 * <p>⚠️ <b>매우 주의해서 다뤄야 하는 인디케이터다.</b> liveness 그룹이 DOWN이 되면 k8s는
 * 컨테이너를 <b>재시작</b>한다. 따라서 여기에는 <b>"재시작하면 해결되는, 복구 불가능한 상태"만</b>
 * 넣어야 한다.
 *
 * <p>넣으면 안 되는 예: DB 일시 장애, 외부 API 타임아웃 같은 <b>일시적/외부적 문제</b>.
 * 이런 것을 liveness에 넣으면 모든 Pod가 동시에 재시작 루프에 빠질 수 있다.
 * (그런 조건은 {@link MyReadinessHealthIndicator} 쪽 — 트래픽만 끊고 재시작하지 않음 — 에 둔다.)
 *
 * <p>넣어도 되는 예: 내부 상태가 회복 불가능하게 오염되어 프로세스를 새로 띄우는 것 외에는
 * 방법이 없는 경우(예: 교착 상태 감지, 필수 백그라운드 스레드가 죽어 되살릴 수 없는 경우).
 *
 * <p>기본값은 UP이며, {@link #markBroken()}이 호출되기 전까지는 절대 DOWN이 되지 않는다.
 * 즉 별도 호출이 없으면 기존 {@code livenessState}와 동일하게 "프로세스가 살아있음"만 의미한다.
 */
@Component("myLiveness")
public class MyLivenessHealthIndicator implements HealthIndicator {

    // 기본값 true(정상). 복구 불가능한 상태가 감지됐을 때만 false로 내려간다.
    private final AtomicBoolean healthy = new AtomicBoolean(true);

    /**
     * 복구 불가능한 상태를 감지했을 때만 호출한다.
     * 호출 즉시 liveness가 DOWN(503)이 되고, k8s가 컨테이너를 재시작한다.
     */
    public void markBroken() {
        healthy.set(false);
    }

    @Override
    public Health health() {
        return healthy.get()
                ? Health.up().build()
                : Health.down().withDetail("reason", "unrecoverable-state").build();
    }
}
