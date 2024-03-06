package JavaToKt_10_object

/**
 * 코틀린은 static 키워드가 없다. 대신 동행 객체 (companion object)를 사용한다.
 *
 * 동반 객체는 하나의 객체로 간주된다. 때문에 이름을 붙일 수도 있고, interface를 구현할 수도 있다.
 */
fun main() {

}

class Person private constructor(
    var name: String,
    var age: Int,
) {
    // 이 객체의 기본 이름은 Companion이다. 즉 외부에선 Person.Companion.create()로 호출할 수 있다.
    companion object {
        private val MIN_AGE = 1 // 런타임시에 값이 할당됨.
        private const val MAX_AGE = 150 // 컴파일시에 값이 할당됨.
        fun create(name: String, age: Int) = Person(name, age)
    }
}

class Person2 private constructor(
    var name: String,
    var age: Int,
) {
    companion object Factory {
        private val MIN_AGE = 1 // 런타임시에 값이 할당됨.
        private const val MAX_AGE = 150 // 컴파일시에 값이 할당됨.
        fun create(name: String, age: Int) = Person2(name, age)
    }
}
