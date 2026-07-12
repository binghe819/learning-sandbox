# =========================================================
# 1) Build stage - gradle wrapper로 bootJar 생성 후 레이어 분리
# =========================================================
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace

# 의존성 캐싱: 빌드 스크립트만 먼저 복사해 의존성 레이어를 소스와 분리
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon > /dev/null 2>&1 || true

# 소스 복사 후 실행 가능한 bootJar 빌드
COPY src ./src
RUN ./gradlew clean bootJar --no-daemon

# Spring Boot layered jar를 레이어별로 추출 (dependencies / loader / snapshot / application)
#   - *-SNAPSHOT.jar 글롭은 -plain.jar(비실행 jar)와 매칭되지 않으므로 bootJar만 선택된다.
RUN java -Djarmode=layertools -jar build/libs/*-SNAPSHOT.jar extract --destination extracted

# =========================================================
# 2) Runtime stage - JRE만 포함 (preStop exec용 sh/sleep 있는 Ubuntu 기반)
# =========================================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 보안: non-root 사용자로 실행
RUN groupadd --system spring && useradd --system --gid spring spring

# 변경 빈도가 낮은 레이어부터 복사해 도커 레이어 캐시 효율을 높인다
COPY --from=builder /workspace/extracted/dependencies/ ./
COPY --from=builder /workspace/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/extracted/application/ ./

USER spring
EXPOSE 8080

# ✅ exec form: java가 PID 1이 되어 SIGTERM을 직접 수신 → Graceful Shutdown이 실제로 동작한다.
#    (shell form으로 쓰면 sh가 PID 1이 되어 SIGTERM이 java에 전달되지 않아 graceful 설정이 무력화된다.)
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
