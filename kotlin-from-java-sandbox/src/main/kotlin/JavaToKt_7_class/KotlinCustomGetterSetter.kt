package JavaToKt_7_class

/**
 * 코틀린은 프로퍼티에 대한 getter, setter를 자동으로 만들어준다.
 * 그리고 여러가지 방법으로 커스텀 getter, setter를 만들 수 있다. 단, 모든 커스텀 getter, setter는 자바로 전환하면 동일한 getter, setter 함수가 만들어진 것을 볼 수 있다.
 */
class KotlinCustomGetterSetter(
    name: String = "binghe",
    val name2: String = "binghe2",
    name3: String,
    var age: Int = 1
) {
    // 만약 name에 대한 get을 외부에서 호출했을 때, 원하는 함수를 호출하고 싶다면 아래와 같이 사용하면 된다.
    // 우선 주 생성자내 val을 없애준다. 그리고 아래와 같이 설정해준다.
    // field를 사용하는 이유는 name 사용시 getter를 호출함으로써 무한루프에 빠지는 것을 방지하기 위함이다. (backing field)
    val name: String = name
        get() = field.uppercase()

    // 위와 같이 backingfield를 사용하지 않는다면 아래와 같이 사용해도 된다.
    val name2Uppercase: String
        get() = this.name2.uppercase()

    // Custom setter - 1
    var name3: String = name3
        set(value) {
            field = value.lowercase()
        }

    // 커스텀 Getter, Setter - 함수로 만드는 방법
    fun isAdult(): Boolean {
        return age >= 20
    }

    // 커스텀 Getter, Setter - 프로퍼티로 만드는 방법
    val isAdult2: Boolean
        get() {
            return this.age > 20
        }

    val isAdult3: Boolean
        get() = this.age > 20
}