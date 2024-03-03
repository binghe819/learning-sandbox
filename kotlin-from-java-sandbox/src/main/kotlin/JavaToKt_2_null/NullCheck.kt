package JavaToKt_2_null

/**
 * 자바에선 reference 타입의 경우 null이 들어 갈 수 있다.
 * 반면에 코틀린은 기본적으로 null이 들어갈 수 없게끔 설계되으며, null을 들어가게하려면 따로 "?"를 붙여줘야하는 것과 같이 명시해주어야한다.
 *
 * < 자바 -> KT >
 * - Boolean (reference type) -> Boolean?
 * - boolean (primitive type) -> Boolean
 *
 */
fun main() {
    val str: String? = null
    println("stringStartsWithKt(str): ${stringStartsWithKt(str)}")
    println("stringStartsWithKt2(str): ${stringStartsWithKt2(str)}")
    println("stringStartsWithKt3(str): ${stringStartsWithKt3(str)}")
}

fun stringStartsWithKt(str: String?): Boolean {
    if (str == null) {
        throw IllegalArgumentException("str is null")
    }

    return str.startsWith("Kt")
}

fun stringStartsWithKt2(str: String?): Boolean? {
    if (str == null) {
        return null
    }

    return str.startsWith("Kt")
}

fun stringStartsWithKt3(str: String?): Boolean {
    if (str == null) {
        return false
    }

    return str.startsWith("Kt")
}
