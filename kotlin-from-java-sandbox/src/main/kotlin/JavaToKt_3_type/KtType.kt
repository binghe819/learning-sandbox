package JavaToKt_3_type

/**
 * < 코틀린에만 존재하는 특이한 타입 3가지 >
 *
 * < Any >
 * - Java의 Object 역할 (모든 객체의 최상위 타입)
 * - 모든 Primitive Type의 최상위 타입도 Any.
 * - Any 자체로는 null을 포함할 수 없어 null을 포함하고 싶은 경우, "Any?"로 표현해야한다.
 * - Any에 equals / hashCode / toString 등의 메소드가 정의되어 있다.
 *
 * < Unit >
 * - Java의 void와 동일한 역할.
 * - void와 다르게 Unit은 그 자체로 타입 인자로 사용 가능하다.
 * - 함수형 프로그래밍에서 Unit은 단 하나의 인스턴스만 갖는 타입을 의미한다.
 *   즉, 코틀린의 Unit은 실제 존재하는 타입이라는 것을 표현한다.
 *
 * < Nothing >
 * - Nothing은 함수가 정상적으로 끝나지 않았다는 사실을 표현하는 역할이다.
 * - 무조건 예외를 반환하는 함수 / 무한 루프 함수등 끝이 좋지 않는 함수에 사용된다.
 */
fun main() {

}

fun nothingExampleFun(message: String): Nothing {
    throw IllegalArgumentException(message)
}

