package JavaToKt_3_type

/**
 *
 */
fun main() {
    printNameLength(Person("Kotlin"))
    printNameLengthNullable(null)
}

fun printNameLength(obj: Any) {
    // 자바의 instanceOf 대신 is를 사용한다.
    if (obj is Person) {
        // 자바의 (Person) obj 대신 as를 사용한다.
        val person = obj as Person
        println(person.name)
        return
    }
    val person = obj as Person
    println(obj.name)
}

fun printNameLengthNullable(obj: Any?) {
    // nullable의 경우 "as?"로해서 null이 아닐때만 캐스팅을 해준다.
    val person = obj as? Person
    println(person?.name)
}
