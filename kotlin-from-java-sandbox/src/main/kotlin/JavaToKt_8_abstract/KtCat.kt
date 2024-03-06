package JavaToKt_8_abstract

/**
 * - extends가 아닌 ":"로 상속을 받는다.
 */
class KtCat(
    species: String,
) : AbstractKtAnimal(species, 4) {

    override fun move() {
        println("고양이가 네 발로 걷습니다.")
    }

}