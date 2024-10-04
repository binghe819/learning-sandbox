package com.binghe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcCoroutineServerApplication

fun main(args: Array<String>) {
    runApplication<GrpcCoroutineServerApplication>(*args)
}