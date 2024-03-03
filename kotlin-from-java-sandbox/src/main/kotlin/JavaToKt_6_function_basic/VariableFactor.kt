package JavaToKt_6_function_basic

/**
 * 가변인자
 */
fun main() {
    printAll("Hello", "World", "Kotlin")

    val array = arrayOf("What", "is", "Kotlin")
    printAll(*array) // 가변인자에 배열을 넣어줘야할 땐 앞에 "*"을 붙여준다.
}

fun printAll(vararg strings: String) {
    for (string in strings) {
        println(string)
    }
}