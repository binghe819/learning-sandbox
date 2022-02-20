package example.exception

import java.io.BufferedReader
import java.io.StringReader

fun readNumberExpression(reader: BufferedReader) {
    val number = try {
        Integer.parseInt(reader.readLine())
    } catch (e: NumberFormatException) {
        null
    }
    println(number)
}

fun main() {
    val reader = BufferedReader(StringReader("숫자 아님"))
    readNumberExpression(reader) // null
}