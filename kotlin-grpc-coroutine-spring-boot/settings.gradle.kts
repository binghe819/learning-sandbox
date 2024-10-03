plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kotlin-grpc-coroutine-spring-boot"
include("untitled")
include("grpc-protocol")
include("grpc-client")
include("grpc-server")
