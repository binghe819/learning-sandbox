package extension

fun main() {
    val list = listOf(1, 2, 3)
//    println(joinToString(list, separator = "; ", prefix = "(", postfix = ")"))
    println(list.joinToString())
    println(list.joinToString(separator = "; ", prefix = "(", postfix = ")"))
}
