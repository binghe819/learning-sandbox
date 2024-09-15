package com.binghe.c_builder

import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

/**
 * async()는 launch()와 거의 유사한데 한 가지 다른 점이라면 주어진 함수의 실행 결과를 반환할 수 있다는 것이다.
 *
 * async()도 코루틴을 제어할 수 있는 객체를 반환하며, 그 객체는 Deferred다.
 * Deferred는 Job의 하위 타입으로 Job과 동일한 기능들이 있고, async()에서 실행된 결과를 가져오는 await() 함수가 추가적으로 존재한다.
 *
 * 보통 async()는 여러 외부 자원을 동시에 호출해야 하는 상황에서 유용하게 활용할 수 있다.
 *
 * 그리고 async()의 가장 큰 장점은 callback을 이용하지 않고도, 비동기 코드를 동기 방식으로 코드를 작성할 수 있게해준다.
 *
 * 한가지 주의할 점은 CoroutineStart.LAZY 옵션을 사용해 코루틴을 지연 실행하면, await() 함수를 호출했을 때 계산 결과를 계속해서 기다린다는 것이다. (블로킹)
 * 그러므로 둘을 같이 사용할 땐, 꼭 start()를 호출하고 await()을 호출해야한다.
 *
 */
fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async { apiCall1() }
        val job2 = async { apiCall2() }
        printWithThread(job1.await() + job2.await())
    }

    printWithThread("소요 시간: $time ms")
}

suspend fun apiCall1(): Int {
    delay(1_000L)
    printWithThread("apiCall1 executed")
    return 1
}

suspend fun apiCall2(): Int {
    delay(1_000L)
    printWithThread("apiCall2 executed")
    return 2
}
