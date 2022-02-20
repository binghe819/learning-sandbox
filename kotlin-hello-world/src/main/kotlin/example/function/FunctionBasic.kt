package example.function

/**
 * 함수 기초
 */
fun main() {
    val a = 5
    val b = 10
    val c = 15

    println(max(a, b))
    println(maxExpression(b, c))
    println(maxExpressionSimple(a, c))
}

fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}

fun maxExpression(a: Int, b: Int): Int = if (a > b) a else b

fun maxExpressionSimple(a: Int, b: Int) = if (a > b) a else b
