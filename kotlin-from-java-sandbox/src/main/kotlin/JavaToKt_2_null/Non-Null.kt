package JavaToKt_2_null

/**
 * < Null 아님 선언 >
 * - 매번 safe call이나 elvis 연산자를 사용할 순 없다..
 * - 코틀린에선 nullable type이지만, null이 절대 들어갈 수 없는 경우 "!!"를 사용하여 null이 들어가지 않음을 명시해줄 수 있다.
 * - 대신 null이 들어가게 되면 KotlinNullPointerException이 발생한다. 그러므로 정말 null이 아님이 확실한 경우에만 사용해야한다.
 */
fun main() {
    println(startsWithKt("Kt binghe"))
    println(startsWithKt(null)) // Error: KotlinNullPointerException
}

fun startsWithKt(str: String?): Boolean {
    return str!!.startsWith("Kt")
}
