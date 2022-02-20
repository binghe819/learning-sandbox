package example.exception

import java.io.BufferedReader
import java.io.StringReader

fun readNumber (reader: BufferedReader): Int? {
    try {
        val line = reader.readLine()
        return Integer.parseInt(line)
    } catch (e: NumberFormatException) {
        return null
    } finally {
        reader.close()
    }
}

fun main() {
    val reader = BufferedReader(StringReader("300"))
    println(readNumber(reader))
}