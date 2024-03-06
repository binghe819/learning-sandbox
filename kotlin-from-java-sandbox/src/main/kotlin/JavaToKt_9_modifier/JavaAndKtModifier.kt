package JavaToKt_9_modifier

/**
 * 자바와 코틀린의 가시성 제어
 *
 * public -> public : 동일하게 모든 곳에서 접근 가능
 * protected -> protected : 자바와 다르게 패키지내에선 접근이 안되고 선언된 클래스 또는 하위 클래스에서만 접근 가능하다.
 *                          코틀린에선 패키지를 namespace를 관리하기 위한 용도로만 사용하지, 자바와 같이 가시성 제어에는 사용되지 않는다.
 * default -> internal : 자바에선 같은 패키지에서만 접근가능했다면 코틀린에선 같은 모듈에서만 접근 가능. (모듈: 한 번에 컴파일 되는 코드)
 * private -> private : 동일하게 선언된 클래스 내에서만 접근 가능
 *
 * 자바의 기본 접근 지시어는 default, 코틀린의 기본 접근 지시어는 public이다.
 *
 */

val num = 3

fun add(a: Int, b: Int) = a + b

class Cat private constructor()