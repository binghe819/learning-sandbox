package JavaToKt_8_abstract

interface KtSwimable {

    val swimAbility: Int
        get() = 3

    fun act() {
        println(swimAbility)
        println("수영합니다.")
    }
}