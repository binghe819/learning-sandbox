package com.binghe.a_routine_coroutine

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/**
 * co-routine은 서로 협력하는 루틴(함수)란 의미이다.
 *
 * 루틴과 코루틴의 가장 큰 차이는 중단과 재개다. 루틴은 한번 시작되면 종료될 때까지 멈추지 않지만, 코루틴은 상황에 따라 잠시 중단이 되었다가 다시 시작되기도 한다.
 *
 * 그리고 코루틴의 함수의 처리가 완전히 종료될 때까지 메모리가 초기화되지않는다.
 *
 * 코루틴 디버깅 : vm option -> "-Dkotlinx.coroutines.debug"
 */
// runBlocking은 일반 루틴 세계와 코루틴 세계를 연결하는 함수다. 새로운 코루틴을 만들게된다.
fun main(): Unit = runBlocking {
    printWithThread("START")
    // launch 함수 역시 새로운 코루틴을 만드는 함수다. 주로 반환 값이 없는 코루틴을 만드는데 사용된다.
    launch {
        newCoRoutine()
    }
    // yield()는 양보하다의 뜻으로 지금 코루틴의 실행을 잠시 멈추고 다른 코루틴이 실행되도록 양보한다.
    yield()
    printWithThread("END")
}

// suspend라는 키워드가 붙으면 다른 suspend fun을 호출하는 특수 능력을 갖게 된다. (일시중단하고 재개 될 수 있는 함수)
suspend fun newCoRoutine() {
    val num1 = 1
    val num2 = 2
    yield()
    printWithThread("${num1 + num2}")
}

fun printWithThread(str: Any) {
    println("[${Thread.currentThread().name}] $str")
}
