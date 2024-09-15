package com.binghe.c_builder

import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Job을 이용한 코루틴 제어중 한 예시로 Join을 살펴본다.
 *
 * 아래 코드에서 join을 빼면 두 코루틴은 동시에 실행되므로 1.1초면 출력된다.
 *
 * 반면, join을 넣으면 job1이 끝나고 job2가 실행되므로 2초가 걸리게된다.
 *
 * 즉, join = 코루틴이 완료될 때까지 대기한다.
 */
fun main(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000)
        printWithThread("Job 1")
    }
    job1.join()

    val job2 = launch {
        delay(1_000)
        printWithThread("Job 2")
    }
}