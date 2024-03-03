package JavaToKt_6_function_basic

/**
 * 자바에선 파라미터의 기본 값을 설정하기위해 여러 생성자 혹은 함수 오버로딩해서 만들었지만,
 * 코틀린에선 함수 선언시 기본 값을 설정할 수 있다.
 */
fun main() {
    repeat("Hello")
    repeat("Hello", useNewLine = false) // named parameter
}

fun repeat(
    str: String,
    num: Int = 3,
    useNewLine: Boolean = true
) {
    for (i in 1..num) {
        if (useNewLine) {
            println(str)
        } else {
            print(str)
        }
    }

}
