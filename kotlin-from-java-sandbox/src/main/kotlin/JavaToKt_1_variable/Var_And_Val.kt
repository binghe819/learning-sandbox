package JavaToKt_1_variable

/**
 * <var과 val>
 * var - 가변 변수 (Variable)
 * val - 불변 변수 (Value)
 */
fun main() {
    var number = 10L // long number = 10L;
    val number2 = 100L // final long number = 100L;

    number = 1_000L
//    number2 = 1_000L // Error: Val cannot be reassigned

    println("number: $number, number2: $number2")
}
