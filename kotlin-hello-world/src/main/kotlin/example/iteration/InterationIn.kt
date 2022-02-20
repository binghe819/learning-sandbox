package example.iteration

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) = c !in '0'..'9'
fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "숫자"
    in 'a'..'z', in 'A'..'Z' -> "문자"
    else -> "몰라"
}

fun main() {
    println(isLetter('q')) // true
    println(isNotDigit('1')) // false
    println(recognize('a')) // 문자
    println(recognize('1')) // 숫자
}