package com.binghe.e_exception_hierarchy

import com.binghe.a_routine_coroutine.newCoRoutine
import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.*

/**
 * 코루틴 예외 처리
 * 1. 발생한 예외가 CancellationException이면 -> 취소로 간주하고 부모 코루틴에게 전파하지 않는다.
 * 2. 다른 예외가 발생하면 -> 실패로 간주하고 부모 코루틴에게 전파한다.
 * 그리고 코루틴에서 어떤 예외던 발생하면 내부적으로 코루틴 "취소됨 상태"로 간주한다.
 */
fun main() {
//    runHierarchy()
//    supervisorJobExample()
//    newRootCoroutine()
    exceptionHandler_try_catch()
}

/**
 * 아래 코드에서 runBlocking으로 만들어진 코루틴을 최상위 코루틴 (root),
 * 최상위 코루틴내에서 launch로 만들어진 2개의 job1, job2 코루틴은 각각 자식 코루틴이 된다.
 *
 * 코루틴 안에서 발생한 예외는 부모 코루틴으로 전파된다.
 *
 */
fun runHierarchy(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        printWithThread("Job 1")
    }

    val job2 = launch {
        delay(1_000L)
        printWithThread("Job 2")
    }

    val exceptionJob = launch {
        throw IllegalArgumentException() // 예외가 부모 코루틴인 runHierarchy로 전파된다.
    }

    delay(1_000L)
    printWithThread("부모 코루틴까지 예외가 전파되어 여기가 실행되지 않는다.")
}

/**
 * SupervisorJob()은 부모 코루틴에게 예외를 전파하지 않는다.
 */
fun supervisorJobExample(): Unit = runBlocking {
    val job = async(SupervisorJob()) {
        throw IllegalArgumentException() // await()을 호출하지 않으면 예외가 무시된다.
    }

    delay(1_000L)
    printWithThread("부모 코루틴까지 예외가 전파되지 않기때문에 여기는 실행된다.")
//    job.await() // await()해야지 예외가 전달된다.
}

/**
 * 만약 새로운 root 코루틴을 만들고싶다면 CoroutineScope으로 코루틴을 생성해주면된다.
 *
 * 그리고 launch는 예외 발생시 예외 출력후 코루틴이 종료된다. 반면, async 함수는 예외가 발생해도 await()을 호출하지않으면 예외가 출력되지 않는다.
 */
fun newRootCoroutine(): Unit = runBlocking {
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        throw IllegalArgumentException("launch는 예외 출력됨") // 예외 출력됨
    }

    val job2 = CoroutineScope(Dispatchers.Default).async {
        throw IllegalArgumentException() // 예외 출력되지 않음
    }

    delay(1_000L)
}
