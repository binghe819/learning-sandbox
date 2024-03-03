package JavaToKt_6_function_basic

/**
 * 함수 선언 문법
 */
fun main() {
    println(max(1, 2))
    println(max2(1, 2))
}

/**
 * 함수 선언
 * - public은 생략가능하다. 즉, 기본이 public이다.
 */
public fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}

// if도 expression이므로 아래와 같이 한 줄로 줄일 수 있다.
fun max2(a: Int, b: Int): Int = if (a > b) a else b
