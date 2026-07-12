package com.binghe.availability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션의 가용성(Availability) 상태가 변경될 때 발생하는 이벤트를 로깅한다.
 *
 * <p>Spring Boot는 내부적으로 두 가지 상태를 관리한다.
 * <ul>
 *   <li>{@link LivenessState} - 애플리케이션이 살아있는지(재시작이 필요한 상태인지) 여부.
 *       BROKEN이면 k8s가 파드를 재시작한다.</li>
 *   <li>{@link ReadinessState} - 애플리케이션이 트래픽을 받을 준비가 되었는지 여부.
 *       REFUSING_TRAFFIC이면 k8s가 Service 엔드포인트에서 파드를 제외한다(재시작하지는 않음).</li>
 * </ul>
 */
@Component
public class AvailabilityChangeListener {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityChangeListener.class);

    @EventListener
    public void onLivenessChange(AvailabilityChangeEvent<LivenessState> event) {
        LivenessState state = event.getState();
        switch (state) {
            case CORRECT -> log.info("[Liveness] CORRECT - 애플리케이션이 정상 동작 중입니다.");
            case BROKEN -> log.error("[Liveness] BROKEN - 애플리케이션이 손상되어 재시작이 필요합니다.");
        }
    }

    @EventListener
    public void onReadinessChange(AvailabilityChangeEvent<ReadinessState> event) {
        ReadinessState state = event.getState();
        switch (state) {
            case ACCEPTING_TRAFFIC -> log.info("[Readiness] ACCEPTING_TRAFFIC - 트래픽을 받을 준비가 되었습니다.");
            case REFUSING_TRAFFIC -> log.warn("[Readiness] REFUSING_TRAFFIC - 트래픽 수신을 거부합니다.");
        }
    }
}
