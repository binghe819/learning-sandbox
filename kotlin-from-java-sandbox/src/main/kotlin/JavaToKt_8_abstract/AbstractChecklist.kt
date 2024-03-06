package JavaToKt_8_abstract

/**
 * 클래스를 상속받을 때 주의할 점
 * - 상위 클래스를 설계할 때 생성자나 초기화 블록에 사용되는 프로퍼티에는 open을 피해야한다.
 *
 * 상속 관련 키워드
 * - final : override를 할 수 없게 한다. default로 보이지 않게 존재한다. 즉, default다.
 * - open : override를 할 수 있게 한다. 상속을 받을 수 있게 한다.
 * - abstract : 상속을 받을 수 있게 한다. 상속을 받을 때 반드시 override를 해야한다.
 * - override : 상속받은 메서드를 재정의한다.
 */
fun main() {
    Derived(300)
}

open class Base(
    open val number: Int = 100
) {
    init {
        println("Base Class")
        println(number)
    }
}

class Derived (
    override val number: Int
) : Base(number) {
    init {
        println("Derived Class")
//        println(number)
    }
}

