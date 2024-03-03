package JavaToKt_4_operator

class Money(
    val amount: Long
) {
    operator fun plus(money: Money): Money {
        return Money(amount + money.amount)
    }
}