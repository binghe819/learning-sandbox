package JavaToKt_3_type

import kotlin.reflect.typeOf

/**
 * < 자바와 코틀린 기본 타입의 차이 >
 * 자바 - 기본 타입간의 변환은 암시적으로 이루어질 수 있다.
 * 코틀린 - 기본 타입간의 변환은 명시적으로 이루어져야 한다.
 */
fun main() {
    // 타입 추론
    val number1 = 3 // Int
    val number2 = 3L // Long
    val number3 = 3.0 // Double
    val number4 = 3.0f // Float

    // 코틀린에선 암시적 타입 변경이 불가능하다. toLong과 같이 명시적으로 변경해주어야 한다.
    val number5: Int = 4
//    val number6: Long = number5 // Type mismatch.
    val number6: Long = number5.toLong()
    println(number5 + number6)

    val number7 = 5
    val number8 = 10
    val result = number7 / number8
    val result2 = number7 / number8.toDouble()
    println(result) // 0 (주의!!)
    println(result2) // 0.5

    // nullable한 타입의 경우, elvis 연산자를 적극 활용하자.
    val number9: Int? = 5
    val number10: Long = number9?.toLong() ?: 0L
    println(number10) // 5
}
