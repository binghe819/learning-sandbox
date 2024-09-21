plugins {
    id("com.google.protobuf") version "0.9.4"
}

sourceSets{
    getByName("main"){
        java {
            srcDirs(
                "build/generated/source/proto/main/java",
                "build/generated/source/proto/main/kotlin"
            )
        }
    }
}

val grpcVersion = "3.19.4"
val grpcKotlinVersion = "1.2.1"
val grpcProtoVersion = "1.44.1"

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$grpcVersion"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcProtoVersion"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}
