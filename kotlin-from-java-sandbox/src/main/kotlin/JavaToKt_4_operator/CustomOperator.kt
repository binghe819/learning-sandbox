package JavaToKt_4_operator

/**
 * 코틀린에서는 객체마다 연산자를 직접 정의할 수 있다.
 */
fun main() {
    val money1 = Money(10L)
    val money2 = Money(20L)

    val money3 = money1 + money2
    println("money3.amount: ${money3.amount}")
}
