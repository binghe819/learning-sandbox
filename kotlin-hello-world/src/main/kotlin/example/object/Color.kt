package example.`object`

import example.`object`.Color.*

enum class Color(
    val r: Int, val g: Int, val b: Int
) {
    RED(255, 0, 0), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), GREEN(0, 255, 0),
    BLUE(0, 0, 255), INDIGO(75,0,130), VIOLET(238, 130, 238); // 세미콜론을 붙여줘야한다.

    fun rgb() = (r * 256 + g) * 256 + b // 메서드 정의
}

fun getMnemonic(color: Color) =
    when (color) {
        RED -> "Richard"
        ORANGE -> "Of"
        YELLOW -> "York"
        GREEN -> "Gave"
        BLUE -> "Battle"
        INDIGO -> "In"
        VIOLET -> "Vain"
    }

fun getMnemonicV2(color: Color) =
    when (color) {
        RED, ORANGE, YELLOW -> "warm"
        GREEN -> "neutral"
        BLUE, INDIGO, VIOLET -> "cold"
    }

fun mix(c1: Color, c2: Color) =
    when (setOf(c1, c2)) {
        setOf(RED, YELLOW) -> ORANGE
        setOf(YELLOW, BLUE) -> GREEN
        setOf(BLUE, VIOLET) -> INDIGO
        setOf(BLUE, VIOLET) -> INDIGO
        else -> throw Exception("없는 색 조합")
    }

fun mixOptimized(c1: Color, c2: Color) =
    when {
        (c1 == RED && c2 == YELLOW) || (c1 == YELLOW && c2 == RED) -> ORANGE
        (c1 == YELLOW && c2 == BLUE) || (c1 == BLUE && c2 == YELLOW) -> GREEN
        (c1 == BLUE && c2 == VIOLET) || (c1 == VIOLET && c2 == BLUE) -> INDIGO
        else -> throw Exception("없는 색 조합")
    }

fun main() {
    val red = getMnemonic(RED)
    println(red)

    val green = getMnemonicV2(GREEN)
    println(green)

    val orange = mix(RED, YELLOW)
    println(getMnemonic(orange))

    val indigo = mixOptimized(BLUE, VIOLET)
    println(getMnemonic(indigo))
}
