package JavaToKt_8_abstract

interface KtFlyable {
    // default 메서드는 앞에 다른 키워드 없이 그냥 메서드를 선언하면 된다.
    fun act() {
        println("날아갑니다.")
    }

    // 인터페이스 추상 메서드는 abstract 키워드를 사용하지 않는다.
    fun fly()
}