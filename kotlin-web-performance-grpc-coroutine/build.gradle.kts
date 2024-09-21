plugins {
    kotlin("jvm") version "2.0.0"
    id("com.google.protobuf") version "0.9.4"
}

allprojects {
    group = "com.binghe"
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("kotlin")
    }

    repositories {
        mavenCentral()
    }

    val grpcVersion = "3.19.4"
    val grpcKotlinVersion = "1.2.1"
    val grpcProtoVersion = "1.44.1"

    dependencies {
        implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
        implementation("io.grpc:grpc-protobuf:$grpcProtoVersion")
        implementation("io.grpc:grpc-stub:$grpcProtoVersion")
        implementation("io.grpc:grpc-netty-shaded:$grpcProtoVersion")
        implementation("com.google.protobuf:protobuf-kotlin:$grpcVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")

        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }
    kotlin {
        jvmToolchain(21)
    }
}
