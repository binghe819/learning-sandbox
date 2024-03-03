package JavaToKt_4_operator

class Person(val age: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        return age == other.age
    }

    override fun hashCode(): Int {
        return age
    }
}