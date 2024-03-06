package JavaToKt_11_other_class


fun handle(enumclass: EnumClass) {
    when (enumclass) {
        EnumClass.A -> println("A")
        EnumClass.B -> println("B")
        EnumClass.C -> println("C")
    }
}

enum class EnumClass(
    val value: String
) {
    A("a"),
    B("b"),
    C("c");
}