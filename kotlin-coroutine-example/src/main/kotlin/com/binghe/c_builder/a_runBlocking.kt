package com.binghe.c_builder

import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 코루틴을 만드는 방법중 하나인 runBlocking.
 *
 * runBlocking 함수는 새로운 코루틴을 만들고 루틴 세계와 코루틴 세계를 이어주는 역할을 한다.
 *
 * 주의할 점은 이름에 blocking이 있다는 점이다. 즉, runBlocking 함수는 호출시 만들어진 코루틴과 그 안에 있는 코루틴이 모두 완료 때까지 호출 스레드를 블락시킨다.
 * 그러므로 프로그램의 진입하는 최초 함수나 테스트 코드에서 활용하는게 좋다. 함부로 막 사용하면 스레드가 블락되어 다른 코드를 실행할 수 없이 프로그램이 멈출 수 있게된다.
 */
fun main() {
    runBlocking {
        printWithThread("START")
        launch {
            delay(2_000L)
            printWithThread("LAUNCH END")
        }
    }

    printWithThread("END")
}
