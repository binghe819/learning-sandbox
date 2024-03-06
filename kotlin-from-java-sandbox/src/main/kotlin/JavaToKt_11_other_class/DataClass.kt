package JavaToKt_11_other_class

/**
 * 데이터 클래스
 * - DTO(Data Transfer Object)로 사용하기 위한 클래스
 * - 데이터, 생성자, getter, equals, hashCode, toString을 구현하고있다.
 */
data class PersonDto(
    val name: String,
    val age: Int
)
