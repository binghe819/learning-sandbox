plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kotlin-web-performance-grpc-coroutine"
include("rpc-protocol")
include("rpc-client")
include("rpc-server")
