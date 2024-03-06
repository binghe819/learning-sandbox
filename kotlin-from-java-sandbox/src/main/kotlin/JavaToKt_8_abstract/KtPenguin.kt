package JavaToKt_8_abstract

/**
 * - 추상 프로퍼티가 아니라면, 상속받을때 open을 꼭 붙어줘야 override할 수 있다.
 */
class KtPenguin(
    species: String
) : AbstractKtAnimal(species, 2), KtSwimable, KtFlyable {

    private val windCount: Int = 2

    override fun move() {
        println("펭귄이 움직입니다")
    }

    override val legCount: Int
        get() = super.legCount + windCount

    override fun act() {
        super<KtSwimable>.act()
        super<KtFlyable>.act()
    }

    override fun fly() {
        println("펭귄이 납니다?")
    }

    override val swimAbility: Int
        get() = 10
}