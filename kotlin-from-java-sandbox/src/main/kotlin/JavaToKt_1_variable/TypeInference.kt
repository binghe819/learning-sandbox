package JavaToKt_1_variable

/**
 * 타입 추론
 * - 코틀린은 컴파일러가 자동으로 값을 추론해준다.
 * - 만약 변수를 선언할 때 초기화를 해주지않으면 타입을 추론할 수 없어서 컴파일 에러가 발생한다.
 *   이 경우 타입을 명시해주어야 한다.
 */
fun main() {
    var number3: Long = 1_000L
    var number4: Long // 변수를 선언만 해주었기 때문에, 명시적으로 타입을 선언해주어야하는 경우.

    println("number3: ${number3}")
//    println(number4) // Error: Variable 'number4' must be initialized (변수 '
}