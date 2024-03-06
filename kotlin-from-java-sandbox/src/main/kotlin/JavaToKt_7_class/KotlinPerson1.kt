package JavaToKt_7_class

/**
 * - 코틀린은 getter, setter를 자동으로 만들어준다.
 */
class KotlinPerson1 constructor(name: String, age: Int) {
    val name: String = name
    var age: Int = age
}
