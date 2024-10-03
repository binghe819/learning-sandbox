package com.binghe.com.binghe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class GrpcCoroutineServerApplication

fun main(args: Array<String>) {
    runApplication<GrpcCoroutineServerApplication>(*args)
}
