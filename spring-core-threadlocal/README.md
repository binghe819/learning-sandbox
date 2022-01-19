# 스프링 코어 - ThreadLocal
> SpringBoot 환경으로 보는 스프링 동시성

본 글은 [김영한님의 스프링 코어 고급편 강의]()를 기반으로 작성되었습니다.

<br>

## 예제 요구사항 - 로그 추적기
* 모든 `public` 메서드의 호출과 응답 정보를 로그로 출력
* 애플리케이션의 흐름을 변경하면 안된다.
  * 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안된다.
* 메서드 호출에 걸린 시간
* 정상 흐름과 예외 흐름 구분
  * 예외 발생시 예외 정보가 남아야한다.
* 메서드 호출의 깊이 표현
* HTTP 요청을 구분
  * HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야한다.
  * 요청 트랜잭션 ID (DB 트랜잭션이 아님)를 남겨야한다.

<br>

> 예시

```shell
정상 요청
[796bccd9] OrderController.request()
[796bccd9] |-->OrderService.orderItem()
[796bccd9] |   |-->OrderRepository.save()
[796bccd9] |   |<--OrderRepository.save() time=1004ms
[796bccd9] |<--OrderService.orderItem() time=1014ms
[796bccd9] OrderController.request() time=1016ms

예외 발생
[b7119f27] OrderController.request()
[b7119f27] |-->OrderService.orderItem()
[b7119f27] | |-->OrderRepository.save() 
[b7119f27] | |<X-OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생! 
[b7119f27] |<X-OrderService.orderItem() time=10ms ex=java.lang.IllegalStateException: 예외 발생! 
[b7119f27] OrderController.request() time=11ms ex=java.lang.IllegalStateException: 예외 발생!
```

<br>

## 설명
토비의 스프링에선 트랜잭션의 정보를 `ThreadLocal`을 이용해 설명한다.

영한님의 강의에선 로깅 추적기를 통해서 로그의 상태 정보인 `트랜잭션 ID`와 `level`이 하나의 스레드동안 유지되도록 전달되는 것을 요구사항으로 주어준다.

단계별 코드는 다음과 같다.

- V0
  - 프로토타입 개발 (로그 추적기 사용전 핵심 로직 구현)
  - [코드]()
- V1
  - 로그 추적기 적용 (로그 추적기의 요구사항 전반을 시킨다)
  - 문제는 `HTTP 요청을 구분`를 지키지못한다. 즉, 요청(로그)에 대한 문맥 (Context)을 저장하지 못해서 HTTP 요청을 구분하지 못한다.
    - 쉽게말해서 HTTP 요청에 대한 특정 값(`TraceId`)을 동기화하지 못한다.
  - [코드]()
- V2
  - 로그 추적기 적용 + 억지로 `HTTP 요청 구분`를 구현한다.
  - 구현 방법은 정말 무식한... `파라미터로 동기화`를 구현한다.
    - 파마리터로 동기화할 상태를 넘기는 방식은 기존의 코드를 깨트린다.
    - 이는 동기화를 위해서 관련된 메서드의 모든 파라미터를 수정해줘야하며, 많은 수정이 발생하게된다.
  - [코드]()






