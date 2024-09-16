package com.binghe.d_cancel

import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.*

/**
 * 더 이상 사용하지 않는 코루틴을 취소하는 것은 컴퓨터 자원을 절약할 수 있기때문에 굉장히 중요하다.
 *
 * 코루틴을 취소하기 위해선 Job 객체의 cancel() 함수를 사용해야한다.
 *
 * 단, 취소 대상인 코루틴도 취소에 협조를 해주어야한다. (협력하는 코루틴만 취소할 수 있다)
 * - delay(), yield()와 같은 kotlinx.coroutines 패키지의 suspend 함수를 사용하면 취소에 협조할 수 있다.
 * - 코루틴이 협력하는 두 번째 방법은, 코루틴 스스로가 본인의 상태를 확인해 취소 요청을 받았을 때 CancellationException을 던지는 방법이다.
 *   - isActive로 코루틴내 직접 상태를 확인해 CancellationException을 던지는 방법. (delay도 사실 CancellationException을 던져 코루틴을 취소시킨다)
 */
fun main() {
//    cancelableExample_1()
    cancelableExample_2()
//    unCancelableExample()
}

// 협조하는 코루틴 - suspend 구현 => Job2만 출력되고 실행이 끝난다.
fun cancelableExample_1(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        printWithThread("Job 1")
    }

    val job2 = launch {
        delay(1_000L)
        printWithThread("Job 2")
    }

    delay(100L)
    job1.cancel()
}

fun cancelableExample_2(): Unit = runBlocking {
    val job = launch (Dispatchers.Default) {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++} 번째 출력!")
                nextPrintTime += 1_000L
            }

            if (!isActive) {
                throw CancellationException()
            }
        }
    }

    delay(100L)
    printWithThread("취소 시작")
    job.cancel()
}

// 협조하지 않는 코루틴 -> cancel을 호출해서 멈추지않는다.
fun unCancelableExample(): Unit = runBlocking {
    val job = launch {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++} 번째 출력!")
                nextPrintTime += 1_000L
            }
        }
    }

    delay(100L)
    job.cancel()
}


