package com.binghe.e_exception_hierarchy

import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.*

fun main() {
//    exceptionHandler_try_catch()
    exceptionHandler_CoroutineExceptionHandler()
}

/**
 * 예외를 핸들링하고 싶다면? -> try-catch
 */
fun exceptionHandler_try_catch(): Unit = runBlocking {
    val job = launch { // async도 동일
        try {
            throw IllegalArgumentException()
        } catch (e: IllegalArgumentException) {
            printWithThread("예외 catch후 정상 종료")
        }
    }
}

/**
 * 예외를 핸들링하고 싶다면 -> CoroutineExceptionHandler (단, launch에만 적용 가능하고, 부모 코루틴이 있으면 동작하지 않는다)
 */
fun exceptionHandler_CoroutineExceptionHandler(): Unit = runBlocking {
    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        printWithThread("예외 처리")
    }

    val job = CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
        throw IllegalArgumentException()
    }

    delay(1_000)
}
