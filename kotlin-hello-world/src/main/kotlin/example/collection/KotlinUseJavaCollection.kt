package example.collection

fun main() {
    val set = hashSetOf(1, 2, 5)
    val list = arrayListOf(1, 2, 5)
    val map = hashMapOf(1 to "one", 2 to "two", 5 to "five")

    // javaClass는 자바의 getClass()에 해당하는 코틀린 코드
    println(set.javaClass) // class java.util.HashSet
    println(list.javaClass) // class java.util.ArrayList
    println(map.javaClass) // class class java.util.hashMap

    val strings = listOf("first", "second", "fivth")
    println(strings.last())

    val numbers = setOf(1, 2, 5)
    println(numbers.maxOrNull());

    println(list)
}