package JavaToKt_2_null

/**
 * < 코틀린에서 null을 다루는 방법 - Safe Call(?.)과 Elvis(?:) >
 * - Safe Call(?.) : null이 아닐 경우에만 호출 (null이 아니면 실행하고, null이면 실행하지 않는다.)
 * - Elvis(?:) : null일 경우 대체값을 지정 (null이면 대체값을 반환하고, null이 아니면 해당 값을 반환한다.)
 */
fun main() {
    /**
     * Safe Call(?.) - null이 아닐 경우에만 호출 (null이 아니면 실행하고, null이면 실행하지 않는다.)
     */
    val str: String? = "binghe"
    val str2: String? = null
//    println(str.length) // Error: Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type String?
    println(str?.length)// 6
    println(str2?.length) // null

    /**
     * Elvis(?:) - null일 경우 대체값을 지정 (null이면 대체값을 반환하고, null이 아니면 해당 값을 반환한다.)
     * (?:이 90도 돌리면 Elvis 머리 스타일과 유사하다고해서.. Elvis 연산자라고 부른다고한다.)
     */
    var str3: String? = null
    println(str3?.length ?: 0) // 0
}

/**
 * Kt스러운 함수 구성
 */
fun stringStartsWithKt4(str: String?): Boolean {
    return str?.startsWith("Kt")
        ?: throw IllegalArgumentException("str is null")
}

fun stringStartsWithKt5(str: String?): Boolean? {
    return str?.startsWith("Kt")
}

fun stringStartsWithKt6(str: String?): Boolean {
    return str?.startsWith("Kt")
        ?: false
}

/**
 * Elvis Early Return
 */
fun calculate(number: Long?): Long {
    number ?: return 0 // early return
    return number
}
