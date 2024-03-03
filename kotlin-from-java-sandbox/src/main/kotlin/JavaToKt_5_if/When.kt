package JavaToKt_5_if

/**
 * 자바의 switch-case문은 코틀린에서 when으로 대체된다.
 */
fun main() {
    println(getGradeWithSwitch(89))
}

fun getGradeWithSwitch(score: Int): String {
    return when (score) {
        in 90..99 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        else -> "F"
    }
}

// 다양한 when 사용법
fun startsWithKt(obj: Any): Boolean {
    return when (obj) {
        is String -> obj.startsWith("Kt")
        else -> false
    }
}

fun judgeNumber(number: Int) {
    when {
        number == 0 -> println("0입니다.")
        number % 2 == 0 -> println("짝수")
        else -> println("홀수")
    }
}
