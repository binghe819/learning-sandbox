package com.binghe.c_builder

import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * launch도 코루틴 빌더로 반환 값이 없는 코드를 실행할 때 사용한다.
 *
 * launch가 runBlocking과 다른 부분은 launch는 만들어진 코루틴을 제어할 수 있는 Job이란 객체를 반환한다.
 * Job을 이용해 해당 코루틴을 시작, 취소, 종료할 수 있다.
 */
fun main(): Unit = runBlocking {

    val job = launch(start = CoroutineStart.LAZY) {
        (1..5).forEach {
            printWithThread(it)
            delay(500)
        }
    }
    job.start()

    delay(1_000L)
    job.cancel()
}