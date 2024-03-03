package JavaToKt_1_variable

/**
 * < 자바에서의 primitive type>
 * - 자바에선 기본형(primitive type)과 참조형(reference type)으로 나뉘며,
 *   연산할 때 참조형을 사용하면 AutoBoxing, UnBoxing이 발생하여 성능이 떨어진다.
 *
 * < 코틀린에서의 primitive type >
 * - 코틀린은 기본형(primitive type)과 참조형(reference type)을 따로 구분하진 않고,
 *   코드상에선 모두 참조형으로 사용하지만, 실제 실행시엔 코틀린이 알아서 primitive type으로 변환하여 사용한다.
 * - 즉, boxing/unboxing을 고려하지 않아도 되도록 Kotlin이 알아서 처리 해준다.
 */
fun main() {
    var number1: Long = 10L

    println("number1: $number1")
}

