package extension

fun main() {
    val view: View = Button()
    view.click() // Button clicked
    view.showOff() // I'm a view!
}

open class View {
    open fun click() = println("View clicked")
}

// Button은 View를 상속받는다.
class Button: View() {
    override fun click() = println("Button clicked")
}

fun View.showOff() = println("I'm a view!")
fun Button.showOff() = println("I'm a button!")
