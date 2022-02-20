package example.`object`

class Rectangle(
    val height: Int,
    val width: Int
) {
    val isSquare: Boolean
        get() = height == width
}

fun main() {
    val square = Rectangle(10, 10)
    val rectangle = Rectangle(10, 15)

    println(square.isSquare) // true
    println(rectangle.isSquare) // false
}
