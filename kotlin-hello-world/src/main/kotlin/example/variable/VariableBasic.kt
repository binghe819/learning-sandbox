package example.variable

/**
 * 변수 기초
 */
fun canPerformOperation() = false;

fun main() {
    val final = 10 // immutable
    var changeable = 1 // mutable

    println(final)
    println(changeable)

    // val이어도 초기화가 한번만 된다면 아래와 같이 사용할 수 있다.
    val message: String
    if (canPerformOperation()) {
        message = "Success"
    } else {
        message = "Failed"
    }
    println(message)
}
