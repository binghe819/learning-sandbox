# Spring Boot + JPA 예제 2
> 본 학습 테스트는 [김영한님의 스프링 부트 + JPA 활용 2편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-API%EA%B0%9C%EB%B0%9C-%EC%84%B1%EB%8A%A5%EC%B5%9C%EC%A0%81%ED%99%94)을 바탕으로 진행된 자료입니다.

<br>

# 목차

<br>

<br>

# API 개발

<br>

## 계층간의 역할을 분리하고 DTO를 활용하여 계층간의 통신을 하자.
* **각 계층간의 역할에 따라 객체를 분리시키는 것이 좋다. -> 계층간의 통신은 가능한 DTO로 하자.**
  * 계층간의 역할을 분리하지않고, 통신하는데 DTO를 사용하지 않으면 발생하는 문제
    * 엔티티에 표현 계층을 위한 로직이 추가되어야한다.. (후..)
    * 각 API 요청별 엔티티의 어떤 값까지 사용하는지 파악하기 어렵다 (ex. 어떤 요청엔 name이 null이 아닌데, 어떤 요청엔 null일 수도..)
    * 기본적으로 엔티티의 모든 값이 노출되게된다. (Response에 엔티티를 그대로 사용한다면)
    * Response 스펙에 따라 엔티티에 로직이 추가될 수 있다. (@JsonIgnore, 혹은 별도의 뷰 로직 등등)
    * 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 표현계층 응답 로직을 담기 어렵다.
    * 엔티티가 변경되면 API 스펙도 변경되게된다. (큰 문제..)
    * 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다. (별도의 Result 클래스 생성으로 해결해야한다.)
  * **결론: 가능하면 계층간의 통신은 DTO를 사용하는 것이좋다. 또한, API의 요청과 응답도 DTO를 사용하자.**
* **응답 DTO는 리스트로 바로 반환하면 유연성이 떨어진다.**
  * 아래와 같이 한차례 감싸서 안에 리스트를 반환하도록 하는 것이 추후 변경에 대한 유지보수에 편한다. (실무에선 특히 더더욱 더!)

```java
@Data
@AllArgsConstructor
static class Result<T> {
    private int count;
    private T data; // 리스트 혹은 특정 DTO.
}
```

<br>

## CQS
* CQS
  * Command과 Query를 철저히 분리시키는 것이 좋다.
  * 유지보수성에 도움이 많이 되기 때문.
  
<br>

# 조회 최적화
90%의 장애는 조회에서 발생한다. 이번 챕터에선 조회 최적화를 어떻게하면 좋을지 다룬다.

<br>

## 조회용 샘플 데이터
조회 최적화를 위해 샘플 데이터를 먼저 만들어본다.

* 회원 두 명이 존재하며, 2명이 책을 두 권씩 구매하는 데이터
  * UserA -> JPA 1 Book, JPA 2 Book
  * UserB -> SPRING 1 Book, SPRING 2 Book

코드는 [InitDB](./src/main/java/com/binghe/springbootjpaexample2/shoppin_mall/InitDb.java)를 참고하면 된다.

<br>

## 지연로딩과 조회 성능 최적화
주문 + 배송정보 + 회원을 조회하는 REST API를 만들어본다.

> 조회시 지연 로딩으로 인한 성능 문제를 단계적으로 해결한다고 보면 된다. 

<br>

### BAD Practice - 엔티티 직접 노출 (무한루프에 빠지게 됨)
아래 예시는 모든 주문 정보를 조회하는 API를 엔티티를 반환하도록 구현한 것이다.

> OrderSimpleApiController.java
```java
@GetMapping("/api/v1/simple-orders")
public List<Order> ordersV1() {
    List<Order> all = orderRepository.findByOrderSearchBadPractice(new OrderSearch());
    return all;
}
```

위 코드를 실행하고 조회하면 무한 루프에 빠지게되며, `StackOverFlow` 예외가 발생하게된다.

그 이유는 무엇일까? 바로 Order와 Member간의 관계에서 무한루프가 발생하기 때문이다.

> Order.java
```java
public class Order {
    ....

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    ....
}
```

> Member.java

```java
import java.util.ArrayList;

public class Member {
    ....

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY
    private List<Order> orders = new ArrayList<>();
    
    ....
}
```

Order를 가져와서 JSON화를 할 때, 모든 상태에 대해서 `getter`를 실행하게 된다.

그리고 이때 지연 로딩이 발생하면서, List<Order>를 호출하고, Order마다의 Member를 호출, Member마다 List<Order> 호출하면서 무한 루프에 빠지게 된다.

* 최초 JPQL을 통한 List<Order> 조회 -> Order마다 Member 조회 -> Member마다 List<Order> 조회 -> 무한루프

<br>

**양방향 관계 문제 해결 방안으로 다음과 같이 생각할 수 있다.**

* 적절히 `@JsonIgnore`를 설정하여 Member에서 List<Order>를 직렬화하지않아 조회하지 않도록 하는 것.
   * 이 방법으로 시도한다고해도 문제가 발생한다.
     * `com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.ArrayList[0]->com.binghe.springbootjpaexample2.shoppin_mall.domain.Order["member"]->com.binghe.springbootjpaexample2.shoppin_mall.domain.Member$HibernateProxy$5gRciVUq["hibernateLazyInitializer"])`
   * **바로 JPA에서 지연 로딩을 위해 프록시를 사용하는데, jackson 라이브러리는 기본적으로 프록시 객체를 어떻게 json으로 생성해야 하는지 모른다. -> 예외 발생.**
     * `Order -> Member`와 `Order -> Address` 관계에서 위 예외가 발생하게 된다.
     * JPA의 경우 지연 로딩의 경우, 해당 상태를 new를 통해 만들지 않고, 프록시로 감싸놓는다. 그리고 지연 로딩될 때 해당 프록시의 타겟 객체로 넣는 형식이다.
* **LAZY로 엔티티 관계를 설정하면 무조건 프록시로 주입되게 된다.** (LAZY로하고 즉시 로딩을 해도 프록시로 주입된다?)

> **엔티티를 그대로 직렬화하고자할 때, LAZY로 인해 프록시객체의 타겟이 없다면 예외가 발생한다.**

<br>

만약 굳이.. 도메인을 표현계층에 노출하고싶다면 Hibernate5Module를 사용해서 해결할 수는 있다.

하지만! **그냥 도메인을 노출하지말고 DTO를 통해 도메인을 감춰라.**

<br>



