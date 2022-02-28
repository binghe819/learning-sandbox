package example.collection

import java.lang.StringBuilder

fun <T> joinToStringV2(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "[",
    postfix: String = "]"
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun main() {
    val list = listOf(1, 2, 5)
    println(joinToStringV2(list)) // [1, 2, 5]
    println(joinToStringV2(list, ";")) // [1;2;5]
    println(joinToStringV2(list, prefix = "{", postfix = "}")) // {1, 2, 5}
}