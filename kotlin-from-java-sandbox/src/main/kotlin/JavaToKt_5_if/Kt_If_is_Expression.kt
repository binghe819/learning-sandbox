package JavaToKt_5_if

/**
 * 자바에서의 if는 statement이지만, 코틀린에서는 expression이다.
 */
fun main() {

}

fun getPassOrFail(score: Int): String {
    // expression이므로 아래와 같이 바로 리턴할 수 있다.
    return if (score >= 60) "Pass" else "Fail"
}
