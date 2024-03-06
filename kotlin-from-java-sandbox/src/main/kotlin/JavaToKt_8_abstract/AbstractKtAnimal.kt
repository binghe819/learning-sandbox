package JavaToKt_8_abstract

abstract class AbstractKtAnimal(
    protected val species: String,
    protected open val legCount: Int
) {
    abstract fun move()
}