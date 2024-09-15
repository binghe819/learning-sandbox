package com.binghe.a_routine_coroutine

/**
 * co-routine에서 routine은 컴퓨터 공학에서 얘기하는 루틴으로 '함수'를 가리킨다.
 *
 * 보통 생각하는 루틴(함수)는 서로 호출과 반환을 주고받으며 협력한다.
 *
 * 다만 루틴(함수)를 진입(호출)하는 곳이 한 곳이며, 루틴이 종료되면 실행 결과를 반환하고 그 루틴에서 사용된 정보가 초기화된다.
 *
 */
fun main() {
    println("START")
    newRoutine()
    println("END")
}

fun newRoutine() {
    val num1 = 1
    val num2 = 2
    println("${num1 + num2}")
}