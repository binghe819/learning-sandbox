package example.`object`

interface Expr
class Num(val value: Int): Expr
class Sum(val left:Expr, val right:Expr): Expr

fun eval(e: Expr): Int {
    if (e is Num) {
        val n = e as Num // 강제 형변환 (is를 사용하면 스마트 캐스트로 인해 불필요하다)
        return e.value
    }
    if (e is Sum) {
        return eval(e.right) + eval(e.left) // 변수 e에 스마트 캐스트를 사용한다.
    }
    throw IllegalArgumentException("알 수 없는 표현")
}

fun evalWhen(e: Expr) =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else -> throw IllegalArgumentException("알 수 없는 표현")
    }

fun main() {
    println(eval(Sum(Sum(Num(1), Num(2)), Num(3)))) // 1 + 2 + 3 = 6
    println(evalWhen(Sum(Sum(Num(1), Num(2)), Num(3)))) // 1 + 2 + 3 = 6
}
