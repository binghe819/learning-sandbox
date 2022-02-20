package example.input_output

fun main() {
    print("이름 > ")
    val name = readLine()

    print("나이 > ")
    val age = readLine() !!.toInt()

    println("입력한 이름: $name, 입력한 나이: $age")
}
