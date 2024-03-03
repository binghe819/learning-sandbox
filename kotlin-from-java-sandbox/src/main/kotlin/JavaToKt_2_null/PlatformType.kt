package JavaToKt_2_null

/**
 * < 코틀린에서 자바 코드를 가져다 사용할 때 어떻게 처리되는 것인가? -> 플랫폼 타입 >
 * - javax.annotation 패키지
 * - android.support.annotation 패키지
 * - org.jetbrains.annotations 패키지
 * 위 패키지에 @Nullable등과 같은 애노테이션이 존재하는데, 자바 코드에 해당 애노테이션을 붙이면 코틀린에서 애노테이션을 인식해서 활용한다.
 * 즉, 자바 코드로만 구성된 라이브러리는 사실 믿고 사용하면 안되고, 중간에 wrapping 역할의 코틀린 코드를 넣거나, 라이브러리 코드를 잘 살펴보고 사용하길 추천한다.
 */
fun main() {
    val person = PlatformType_Member("binghe")
    println(platformStartsWithKt(person.name))
}

fun platformStartsWithKt(str: String): Boolean {
    return str.startsWith("Kt")
}
