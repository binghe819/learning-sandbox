package JavaToKt_2_null

/**
 * < 코틀린에서는 기본적으로 null이 들어갈 수 없게끔 설계되었다 >
 * - 만약 null이 변수에 들어갈 수 있게할려면 타입 뒤에 ?를 붙여주어야 한다.
 *
 * 코틀린에선 null 사용을 안전하게 하기 위해서 null이 들어갈 수 있는 변수를 완전히 다른 타입으로 간주한다.
 */
fun main() {
    // null 삽입 불가
    var number1 = 10L
//    number1 = null // Error: Null can not be a value of a non-null type Long

    // null 삽입 가능 - ?사용하면 가능하다.
    var number2: Long? = 10L
    number2 = null

    println("number1: $number1, number2: $number2")
}