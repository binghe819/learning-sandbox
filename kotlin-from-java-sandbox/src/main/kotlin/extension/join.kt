package extension

val UNIX_LINE_SEPARATOR = "\n"

fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) { // 첫 원소 앞에는 구분자 붙이지 않기 위함.
            result.append(separator)
        }
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}
