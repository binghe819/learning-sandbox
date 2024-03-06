package extension

fun main() {
    val str = "Kotlin"
    println(str.lastChar())
}

fun String.lastChar(): Char = this.get(this.length - 1)
