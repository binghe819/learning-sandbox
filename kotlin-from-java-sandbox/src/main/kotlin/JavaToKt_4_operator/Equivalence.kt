package JavaToKt_4_operator

/**
 * Java에서의 동일성은 ==, 동등성은 equals로 구분했다.
 * 코틀린에서는 동일성은 ===, 동등성은 ==를 호출하면 간접적으로 equals를 호출한다.
 */
fun main() {
    val person1 = Person(1)
    val person2 = person1
    val person3 = Person(1)

    // 동일성 비교
    println(person1 === person2) // true
    println(person1 === person3) // false

    // 동등성 비교
    println(person1 == person2) // true
    println(person1 == person3) // true
}

