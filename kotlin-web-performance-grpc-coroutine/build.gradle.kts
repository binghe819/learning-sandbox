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

    dependencies {
        implementation("io.grpc:grpc-kotlin-stub:1.4.1")
        implementation("io.grpc:grpc-protobuf:1.57.1")
        implementation("com.google.protobuf:protobuf-kotlin:4.27.2")

        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }
    kotlin {
        jvmToolchain(21)
    }
}
