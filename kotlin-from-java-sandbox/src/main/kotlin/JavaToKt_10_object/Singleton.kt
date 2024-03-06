package JavaToKt_10_object

/**
 * 코틀린에선 object 키워드를 사용해 쉽게 싱글톤을 만들 수 있다.
 */

fun main() {
    Singleton.print()
    val singleton = Singleton
    singleton.print()
}

object Singleton {
    fun print() {
        println("Singleton")
    }
}