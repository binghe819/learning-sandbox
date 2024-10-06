plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("io.github.lognet.grpc-spring-boot") version "5.1.5"
}

val grpcVersion = "3.19.4"
val grpcKotlinVersion = "1.2.1"
val grpcProtoVersion = "1.44.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":grpc-protocol"))
    implementation("org.springframework.boot:spring-boot-starter")
//    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")
    implementation ("io.github.lognet:grpc-spring-boot-starter:5.1.5") // grpc - spring 연결다리
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion") // grpc - kotlin 연결다리
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2") // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor") // coroutine - reactor 연결다리
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc") // r2dbc
    implementation("io.asyncer:r2dbc-mysql")

    runtimeOnly("com.mysql:mysql-connector-j")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

kotlin {
    jvmToolchain(21)
}
