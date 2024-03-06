package JavaToKt_7_class

/**
 * init -> 코틀린 인스턴스가 생성될 때 최초로 호출되는 블록
 * constructor -> 주 생성자외 다른 생성자를 구성하고 싶은 경우 사용하는 부생성자 키워드.
 *                코틀린에서 주 생성자는 반드시 존재해야한다.
 *                그리고 결국은 주 생성자를 호출함으로써 인스턴스가 생성된다.
 * -> 코틀린에선 부 생성자보단 default parameter에 디폴트값 설정하는 것을 권장한다.
 * -> Converting (A -> B)와 같은 경우 부 생성자를 사용할 수 있지만, 그보다는 정적 팩토리 메서드를 추천한다. (부생성자는 거의 사용할 일이 없다.)
 */
class KotlinPerson3(
    // 주 생성자
    val name: String = "binghe",
    var age: Int = 1
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("age는 0보다 커야합니다.")
        }
        println("초기화 블록이 호출되었습니다.")
    }

    // 부 생성자
    constructor(name: String): this(name, 1) {
        println("첫번째 부 생성자가 호출되었습니다.")
    }

    constructor(): this("binghe") {
        println("두번째 부 생성자가 호출되었습니다.")
    }
}