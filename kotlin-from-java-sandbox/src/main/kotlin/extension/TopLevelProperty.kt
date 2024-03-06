package extension

var opCount = 0

fun performOperation() {
    opCount++
}

fun reportOperationCount() {
    println("Operation performed $opCount times")
}

fun main() {
    performOperation()
    reportOperationCount()
}
