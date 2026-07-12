package com.binghe.shutdown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 종료(shutdown) 과정을 <b>Spring 라이프사이클 관점</b>에서 로깅한다. k8s가 파드를 내릴 때
 * 애플리케이션이 실제로 언제부터 종료 절차에 들어가는지 로그로 관찰하기 위한 학습용 컴포넌트다.
 *
 * <p><b>흐름:</b> k8s는 정상 종료 시 컨테이너 프로세스에게 <b>SIGTERM</b>을 보낸다. 이 시그널을
 * 받은 JVM은 셧다운 훅을 실행하고, 그 안에서 Spring이 {@code ApplicationContext.close()}를
 * 호출한다. 이때 발행되는 {@link ContextClosedEvent} 시점이 곧 <b>graceful shutdown 단계의
 * 시작점</b>이다. 이 직후 웹서버가 새 요청 수신을 멈추고 in-flight 요청을
 * {@code timeout-per-shutdown-phase} 동안 기다린다(application.yml 참고).
 *
 * <p><b>⚠️ SIGKILL(kill -9)로 죽으면 이 로그는 남지 않는다.</b> SIGKILL/SIGSTOP은 커널이
 * 프로세스를 즉시 죽여 셧다운 훅이 실행되지 않기 때문이다. k8s는
 * {@code terminationGracePeriodSeconds}가 지나도 종료가 안 끝나면 SIGKILL을 보내므로,
 * <b>"이 종료 로그가 정상적으로 끝까지 찍혔다 = graceful shutdown이 제한 시간 안에 완료됐다"</b>는
 * 신호로 읽을 수 있다. 반대로 로그가 중간에 끊겨 있으면 SIGKILL 강제 종료를 의심해야 한다.
 *
 * <p>정상 종료 시 기대되는 로그 순서:
 * <pre>
 *   [Readiness] REFUSING_TRAFFIC ...             ← 트래픽 수신 거부로 전환 (AvailabilityChangeListener)
 *   [Shutdown] ContextClosedEvent - graceful ... ← 새 요청 중단, in-flight 요청 완료 대기
 *   (JVM 정상 종료)
 * </pre>
 */
@Component
public class ShutdownLoggingListener {

    private static final Logger log = LoggerFactory.getLogger(ShutdownLoggingListener.class);

    /**
     * Spring이 컨텍스트를 닫기 시작하는 시점. graceful shutdown 단계의 시작점으로 볼 수 있다.
     * 이 직후 웹서버가 새 요청 수신을 멈추고 in-flight 요청 완료를 기다린다.
     */
    @EventListener
    public void onContextClosed(ContextClosedEvent event) {
        log.info("event: {}", event);
        log.info("[Shutdown] ContextClosedEvent - graceful shutdown 시작: "
                + "새 요청 수신 중단, in-flight 요청 완료 대기(timeout-per-shutdown-phase).");
    }
}
