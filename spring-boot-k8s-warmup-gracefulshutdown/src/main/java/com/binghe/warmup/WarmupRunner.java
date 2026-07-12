package com.binghe.warmup;

import com.binghe.health.MyReadinessHealthIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * 자가 호출(self-call) 방식의 웜업 러너 (문서의 "패턴 A - 권장").
 *
 * <p>Spring Boot 기동 순서상 {@link ApplicationRunner}는 {@code ApplicationReadyEvent}보다
 * <b>먼저</b> 실행된다. 따라서 이 run()이 끝나기 전까지 {@code /actuator/health/readiness}는
 * {@code OUT_OF_SERVICE}(503)를 반환하고, k8s readinessProbe도 실패 상태를 유지하므로
 * 트래픽이 유입되지 않는다. 별도의 상태 관리 코드 없이 <b>"웜업 완료 → readiness UP"</b> 순서가
 * 프레임워크 차원에서 보장된다.
 *
 * <p>이 시점에는 이미 웹서버 포트가 열려 있고 liveness는 UP이므로, 자기 자신의 HTTP 엔드포인트를
 * 그대로 호출해 실제 요청 경로(DispatcherServlet → 컨트롤러 → 직렬화)를 태울 수 있다.
 *
 * <p>추가로, 웜업 완료를 <b>명시적 판정 기준</b>으로 노출하기 위해 웜업이 끝나면
 * {@link MyReadinessHealthIndicator#markReady()}를 호출한다. 이 인디케이터는 readiness 그룹에
 * 편입돼 있으므로(application.yml 참고), 웜업이 끝나야 커스텀 인디케이터까지 UP이 되어
 * readiness가 200을 반환한다.
 */
@Component
public class WarmupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(WarmupRunner.class);

    /** JIT 컴파일 효과를 보려면 수백 회 이상이 필요한 경우가 많다. 실측(웜업 직후 응답시간)으로 조정한다. */
    private static final int WARMUP_COUNT = 500;

    private final RestClient client;
    private final MyReadinessHealthIndicator readinessIndicator;

    public WarmupRunner(@Value("${server.port:8080}") int port,
                        MyReadinessHealthIndicator readinessIndicator) {
        this.client = RestClient.create("http://localhost:" + port);
        this.readinessIndicator = readinessIndicator;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("[Warmup] start - self-call {} times", WARMUP_COUNT);
        long startedAt = System.currentTimeMillis();

        int success = 0;
        for (int i = 0; i < WARMUP_COUNT; i++) {
            try {
                // 트래픽이 가장 많은 핵심 조회성 API 위주로 웜업 대상을 고른다.
                client.get()
                        .uri("/api/products/{id}", 1)
                        .retrieve()
                        .toBodilessEntity();
                success++;
            } catch (Exception e) {
                // 웜업은 최선 노력(best-effort). 실패가 기동 실패로 이어지지 않도록 로그만 남긴다.
                log.warn("[Warmup] request #{} failed: {}", i, e.getMessage());
            }
        }

        // 웜업 완료를 커스텀 readiness 인디케이터에 반영한다.
        // 이 호출로 myReadiness가 UP이 되고, readinessState도 곧이어(ApplicationReadyEvent) UP이 되어
        // /actuator/health/readiness 그룹 전체가 200을 반환하게 된다.
        readinessIndicator.markReady();

        long elapsed = System.currentTimeMillis() - startedAt;
        log.info("[Warmup] done - success {}/{}, elapsed {}ms. readiness will now turn UP.",
                success, WARMUP_COUNT, elapsed);
    }
}
