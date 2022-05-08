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

## 지연로딩과 조회 성능 최적화 (Many To One, One To One)
주문 + 배송정보 + 회원을 조회하는 REST API를 만들어본다.

> 조회시 지연 로딩으로 인한 성능 문제를 단계적으로 해결한다고 보면 된다. 

<br>

### 첫번째 BAD Practice - 엔티티 직접 노출 (무한루프에 빠지게 됨)
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

**결론**

* **엔티티를 표현 계층에 직접 노출할 때는 양방향 연관관계가 걸린 곳에서 한 곳은 꼭 `@JsonIgnore`처리 해줘야 한다. 아니면 무한 루프에 빠지게 된다.**
  * Jackson과 같은 직렬화 라이브러리가 모든 상태에 대해서 `getter`를 호출하기 때문에 무한 루프에 빠지는 것. (양방향이므로)
* **엔티티를 표현 계층에 노출하는 것 자체가 문제다... 그냥 DTO로 변환해서 반환하자!!**
* 지연 로딩을 피하기 위해 즉시 로딩으로 설정하면 안된다.
  * 즉시 로딩으로 인해 특정 요청마다 필요없는 상태까지 DB로부터 조회를 실행하기때문에 성능적인 이슈가 발생할 확률이 높다.
  * 항상 기본은 지연 로딩으로 하고, 성능 최적화가 필요한 경우 fetch join을 사용해라

<br>

### 두번째 개선하기 - 엔티티를 DTO로 변환 (N + 1 문제 발생)

> OrderSimpleApiController.java
```java
/**
 * 첫번째 개선 - 엔티티를 DTO로 변환
 */
@GetMapping("/api/v2/simple-orders")
public List<SimpleOrderDto> ordersV2() {
    List<Order> orders = orderRepository.findByOrderSearchBadPractice(new OrderSearch());
    return orders.stream()
            .map(SimpleOrderDto::new)
            .collect(Collectors.toList());
}

@Data
static class SimpleOrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName(); // 쿼리 한번
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress(); // 쿼리 한번
    }
}
```

위와 같이 API 스펙 (SimpleOrderDto)를 정해서 변환하여 반환한다면 더이상 무한 루프등에 빠지지않는다.

> 물론 도메인을 컨트롤러 (표현계층)에 노출한 것은 문제다. 하지만 여기선 그 내용이 메인이 아니므로 우선은 무시한다.

**위 코드도 문제점은 있다. 바로 쿼리가 너무 많이 날라간다는 것이다.**

실제로 위 코드를 동작시켜서 요청을 날려보면 아래와 같이 쿼리가 날라간다. (여기선 Order가 2개 들어가있으므로 총 5번 날라간다.)

**총 쿼리 수 = 모든 Order 찾는 쿼리 1번 + Order마다 Member 한번과 Address 한번 (총 2번) * Order 개수만큼**

만약 1000건의 Order가 존재한다면 1 + (1000 * 2)번의 쿼리가 날라간다.. (이게 그 유명한 N + 1 문제이다.)

> 물론 같은 멤버나 주소가 존재한다면 쿼리가 조금 적게 나갈 수 있다. (하지만.. 그 수가 얼마나 될지는 예상하기 쉽지 않다.)

<br>

### 세번째 개선하기 - Fetch Join 최적화 (N + 1 해결)
> OrderSimpleApiController.java
```java
/**
 * 두번째 개선 - fetch join
 */
@GetMapping("/api/v3/simple-orders")
public List<SimpleOrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithMemberAndDelivery();
    return orders.stream()
            .map(SimpleOrderDto::new)
            .collect(Collectors.toList());
}
```

> OrderRepository.java

```java
class OrderRepository {
    
    ...
  
    public List<Order> findAllWithMemberAndDelivery() {
      return entityManager.createQuery(
              "select o from Order o" +
                      " join fetch o.member m" +
                      " join fetch o.delivery d",
              Order.class
      ).getResultList();
    }
}
```

위와 같이 fetch join을 사용하면 Order를 가져올 때 Member와 Delivery를 하나의 쿼리로 가져온다. (쿼리 1번 날라간 것)

<br>

### 네번째 개선하기 - JPA에서 DTO로 바로 조회
Fetch Join으로도 조회에 대한 대부분의 문제는 해결할 수 있다.

다만 더 최적화를 시키기위해선 특정 요청에 필요없는 데이터까지 가져오지 않도록 하는 것이다.

즉, 엔티티를 조회해서 DTO로 변환하는 것이 아니라, 바로 DTO의 상태에 있는 데이터만을 바로 가져오는 것이다.

<br>

> OrderSimpleApiController.java
```java
/**
 * 세번째 개선 - DTO로 바로 가져오기 (원하는 상태만 조회 -> 원하는 값만 SELECT)
 */
@GetMapping("/api/v4/simple-orders")
public List<OrderSimpleQueryDto> ordersV4() {
    return orderRepository.findOrderDtos();
}
```

> OrderSimpleQueryDto.java
```java
@Data
public class OrderSimpleQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```

> OrderRepository.java
```java
class OrderRepository {
    ...

    public List<OrderSimpleQueryDto> findOrderDtos() {
      return entityManager.createQuery(
              "select new com.binghe.springbootjpaexample2.shoppin_mall.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                      " from Order o" +
                      " join o.member m" +
                      " join o.delivery d",
              OrderSimpleQueryDto.class
      ).getResultList();
    }
    
    ...
}
```

위 API를 실행하면 쿼리가 조금 짧아진다. 그 이유는 DTO에 존재하는 원하는 상태만을 SELECT해서 가져오기 때문이다.

이를 통해 **네트워크적 비용을 조금 줄일 수 있다. - 장점**

다만, **V3에서의 엔티티를 조회하는 것보다 재사용성 측면에서 굉장히 비효율적이다. - 단점**

즉, 각각의 API별로 Repository에 JPQL로 쿼리를 작성해야되는 문제가 있다.

다시 말해, **Repository의 메서드가 표현 계층에 의존하고 있는 것이다. 이는 논리적으로 계층 구조가 깨지는 문제가 있다.**

> 필자의 경우는 보통 V3로 코드를 작성한다. (요청마다 DTO에 맞게 쿼리를 작성하는 것은 유지보수나 확장성 방면에서 너무 취약하다.)

<br>

### 쿼리 방식 선택 권장 순서 (결론)
1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
2. 필요하면 Fetch Join으로 성능을 최적화 한다. -> 대부분의 성능 이슈가 해결된다.
3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다. (요청에 맞는 최적의 쿼리를 작성하는 것.)
   * 이때 쿼리용 패키지를 따로 두는 편이 유지보수성 측면에서 용이하다. (ex. `OrderSimpleQueryRepository`)
   * 엔티티에 의존적인 쿼리 (기존의 JPA 조회)와 표현계층에 의존적인 쿼리를 클래스 분리 시키는 것.
     * 엔티티에 의존적인 쿼리는 재사용 가능.
     * 표현계층에 의존적인 쿼리는 재사용 불가능.

<br>

## 컬렉션 조회 최적화 (One To Many, Many To Many)
위에선 xxxToOne (Many To One, One to One) 관계에서의 Fetch join만을 다뤘다.

toOne 관계에서는 사실 Fetch Join만으로 쉽게 문제를 해결할 수 있다.

이번엔 컬렉션 조회를 의미하는 xxxToMany (One To Many, Many To Many)관계의 조회를 다룬다.

<br>

**예시 - 주문내역에서 추가로 주문한 상품 정보를 조회해본다.**

* `Order` 기준으로 컬렉션인 `OrderItem`과 `Item`이 필요하다.
  * `Order N : N Item` 관계라고 보면 된다.

<br>

### 첫번째 Bad Practice - 엔티티 직접 노출
이전에도 살펴보았듯이, 엔티티를 표현 계층에 그대로 노출하는 것은 좋지 않다.

```java
@GetMapping("/api/v1/orders")
public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAll();
    // Order의 지연 로딩된 상태 초기화
    for (Order order : all) {
        order.getMember().getName();
        order.getDelivery().getAddress();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.getItem().getName();
        }
    }
    return all;
}
```
* 위 코드가 잘 동작하기 위해선 아래 설정을 해주어야한다.
  * Hibernate5Module 모듈 등록 -> LAZY = null 처리
  * Jackson에서 getter를 호출하기 때문에 양방향 관계 문제가 발생한다. 그러므로 모든 호출되는 관계에서 `@JsonIgnore`설정을 해주어야한다.
* **위 방식은 도메인을 그대로 노출하는 것이기때문에 그냥 사용하지 않는 것이 좋다.**

<br>

### 두번째 개선 - 엔티티를 DTO로 변환
이번에도 동일하게 엔티티를 DTO로 만들어서 반환해본다.

```java
@GetMapping("/api/v2/orders")
public List<OrderDto> ordersV2() {
    List<Order> all = orderRepository.findAll();
    return all.stream()
            .map(OrderDto::new)
            .collect(toList());
}

@Data
static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        // 지연 로딩된 부분을 직접 호출하여 채워주는 코드.
        orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(toList());
    }
}

@Data
static class OrderItemDto {

    private String itemName;//상품 명
    private int orderPrice; //주문 가격
    private int count;      //주문 수량

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }
}
```
* 문제점 - 지연로딩으로 인해 `N + 1` 문제 발생 (쿼리가 끔찍하게 많이 나가게된다)
  * `OrderDto`에서 `orderItems`를 초기화하는 부분을 보면 직접 전부 호출해서 데이터를 가져오는 부분이 있다.
  * 이때 `OrderItem`별로 쿼리를 한번씩 날리기때문에 `N + 1` 문제가 발생한다.
  * Order 목록 1번 + Order 목록마다 (Member 조회 1번 + Delivery 조회 1번 + orderItem 목록 1번 + orderItem과 관계를 맺은 Item별 1번씩)

<br>

> 주의할 점!! -> DTO안에 엔티티를 가지도록하면 안된다. DTO는 DTO끼리만 관계를 맺어야한다. (엔티티가 표현계층에 노출되기 때문!)

<br>

### 세번째 개선 - Fetch Join을 통한 최적화
`N + 1` 문제를 해결하기 위해 Fetch Join을 사용해본다.

> OrderApiController.java

```java
@GetMapping("/api/v3/orders")
public List<OrderDto> ordersV3() {
    List<Order> all = orderRepository.findAllWithItem();
    return all.stream()
            .map(OrderDto::new)
            .collect(toList());
}
```

> OrderRepository.java

```java
public List<Order> findAllWithItem() {
    return entityManager.createQuery(
            "select distinct o from Order o" +
                    " join fetch o.member m" +
                    " join fetch o.delivery d" +
                    " join fetch o.orderItems oi" +
                    " join fetch oi.item i", Order.class
    ).getResultList();
}
```
* Fetch Join으로 인해 1번의 쿼리만으로 원하는 정보를 가져올 수 있다.
* distinct를 사용한 이유
  * 데이터 뻥튀기 문제를 해결하기위해 사용한 것.
  * 1:N 관계에서 DB 입장에서 N 개수만큼 뻥튀기가 된다.
  * JPA의 distinct는 SQL에 distinct를 추가해주고, 애플리케이션에서 엔티티의 중복도 제거해준다.
* 위 쿼리에 두 가지 문제점이 존재한다.
  1. 컬렉션 페치 조인과 페이징 -> 즉, 위 쿼리는 페이징이 불가능하다.
  2. 컬렉션 페치 조인은 1개만 사용할 수 있다. -> 즉, 위 쿼리는 데이터 부정합이 발생할 확률이 높다.

<br>

**컬렉션 페치 조인과 페이징**

만약 컬렉션 패치 조인을 한 상태에서 페이징을 사용할 경우 어떻게 될까?

아래와 같이 임시로 OrderRepository에 100개의 Order를 가져오는 페이징 쿼리를 넣어본다.

> OrderRepository.java
```java
public List<Order> findAllWithItem() {
    // 컬렉션 패치 조인 + 페이징 테스트용
    return entityManager.createQuery(
            "select distinct o from Order o" +
                    " join fetch o.member m" +
                    " join fetch o.delivery d" +
                    " join fetch o.orderItems oi" +
                    " join fetch oi.item i", Order.class)
            .setFirstResult(1)
            .setMaxResults(100)
            .getResultList();
}
```
* **결과적으로 페이징이 불가능하다.**
  * **쿼리를 날린 로그를 잘 살펴보면 limit과 offset이 전혀 걸리지 않는다.**
  * **컬렉션 패치 조인을 사용하면 페이징이 불가능하다. 실제 페이징 쿼리 자체가 날라가지 않는다.** 
  * **만약 페이징을 한다면 Hibernate에서 경고 로그를 남기면서 모든 데이터를 DB에서 읽어오고, 메모리에서 페이징 해버린다.** (매우 위험하다.)
* 페이징이 불가능한 이유는 간단하다.
  * **1:N 관계에서 컬렉션 패치 조인의 경우 DB의 결과가 N의 개수만큼 돌아오게된다. 1에 해당하는 엔티티가 데이터 뻥튀기가 발생하기때문에 어디까지가 해당 엔티티의 끝인지 알 수 없다.**
  * 그러기때문에 JPA는 모든 데이터를 다 가져와서 distinct를 진행하여 데이터 뻥튀기를 해결하고 페이징 처리를 진행하는 것이다. -> 문제는 애플리케이션 메모리에 1에 해당하는 엔티티의 모든 데이터를 가져온다는 것...
  * 즉, **데이터 뻥튀기로 인해 페이징에 대한 정확한 기준이 잡히지 않아 페이징이 불가능한 것.**

<br>

**컬렉션 페치 조인은 1개만 사용할 수 있다.**

* 컬렉션 둘 이상에 페치 조인을 사용하면 안된다.
  * **그 이유는 데이터 뻥튀기로 인해 데이터 부정합이 발생할 수 있기 때문이다.**
  * 실제로 1 : N 에서 페치 조인을 사용해도 데이터 뻥튀기가 발생하는데... 1 : N 관계에서 여러번 페치 조인을 사용하면 페치 조인을 실행한만큼 데이터 뻥튀기가 발생한다. (데이터 폭발..)

<br>




