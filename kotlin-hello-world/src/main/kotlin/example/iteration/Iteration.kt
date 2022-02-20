package example.iteration

fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz "
    i % 3 == 0 -> "Fizz "
    i % 5 == 0 -> "Buzz "
    else -> "$i "
}

fun main() {
    val oneToHundred = 1 .. 100

    for (i in oneToHundred) {
        print(fizzBuzz(i))
    }
}


