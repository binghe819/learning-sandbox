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

## ThreadLocal이 필요한 이유 - 동기화
토비의 스프링에선 `Service` 계층에서 트랜잭션을 시작해서 `Repository`계층까지 트랜잭션 정보를 공유하기 위해 `ThreadLocal`의 필요성을 설명한다.

영한님의 강의에선 로깅 추적기를 통해서 로그의 상태 정보인 `트랜잭션 ID`와 `level`이 하나의 스레드동안 유지되도록 전달되는 것을 요구사항으로 주어준다.

단계별 코드는 다음과 같다.

- V0
  - 프로토타입 개발 (로그 추적기 사용전 핵심 로직 구현)
  - [코드](./src/main/java/com/binghe/springcorethreadlocal/app/v0)
  - [로그 추적기](./src/main/java/com/binghe/springcorethreadlocal/trace/hellotrace/HelloTraceV1.java)
- V1
  - 로그 추적기 적용 (로그 추적기의 요구사항 전반을 시킨다)
  - 문제는 `HTTP 요청을 구분`를 지키지못한다. 즉, 요청(로그)에 대한 문맥 (Context)을 저장하지 못해서 HTTP 요청을 구분하지 못한다.
    - 쉽게말해서 HTTP 요청에 대한 특정 값(`TraceId`)을 동기화하지 못한다.
  - [코드](./src/main/java/com/binghe/springcorethreadlocal/app/v1)
  - [로그 추적기](./src/main/java/com/binghe/springcorethreadlocal/trace/hellotrace/HelloTraceV1.java)
- V2
  - 로그 추적기 적용 + 억지로 `HTTP 요청 구분`를 구현한다.
  - 구현 방법은 정말 무식한... `파라미터로 동기화`를 구현한다.
    - 파마리터로 동기화할 상태를 넘기는 방식은 기존의 코드를 깨트린다.
    - 이는 동기화를 위해서 관련된 메서드의 모든 파라미터를 수정해줘야하며, 많은 수정이 발생하게된다.
  - [코드](./src/main/java/com/binghe/springcorethreadlocal/app/v2)
  - [로그 추적기](./src/main/java/com/binghe/springcorethreadlocal/trace/hellotrace/HelloTraceV2.java)
- V3 - 동시성 문제 발생하는 예시
  - `파라미터로 동기화`하는 문제를 해결하기위해 무작정 싱글톤인 `LogTrace` 구현체에 HTTP 요청에 대한 특정 값(`TraceId`)을 만들어 공유해본다.
    - Spring의 Bean은 기본적으로 싱글톤으로 생성된다.
    - 물론 Tomcat의 경우 요청을 Thread기반으로 처리하기 때문에 동기화 문제가 발생한다. (ThreadLocal의 필요성을 느끼기위한 예시)
    - `GET /v3/request`에 연속해서 요청을 보내면 다른 스레드에서 같은 `LogTrace`의 상태를 공유함으로써 동시성 이슈가 발생하는 것을 볼 수 있다.
  - [코드](./src/main/java/com/binghe/springcorethreadlocal/app/v3)
  - [로그 추적기](./src/main/java/com/binghe/springcorethreadlocal/trace/logtrace/FieldLogTrace.java)

<br>

## ThreadLocal

<br>

### ThreadLocal 소개
* Java.lang 패키지에서 제공하는 쓰레드 범위 변수. 한 스레드에서 공유할 변수.
  * 쓰레드 수준의 데이터 저장소이다. (각 쓰레드마다 별도의 내부 저장소)
  * 같은 쓰레드 내에서만 공유
  * 따라서 같은 쓰레드라면 해당 데이터를 메서드 매개변수로 넘겨줄 필요 없다.
* 예시 코드
  * [동시성 이슈 발생 테스트 - ThreadLocal을 사용하지 않은 코드](./src/test/java/com/binghe/springcorethreadlocal/trace/threadlocal/code/FieldService.java)
  * [동시성 이슈 발생하지 않는 테스트 - ThreadLocal 사용한 코드](./src/test/java/com/binghe/springcorethreadlocal/trace/threadlocal/code/ThreadLocalFieldService.java)
  * 그저 ThreadLocal로만 감싸주기만하면 동시성 문제가 발생하지 않는다.

<br>

### ThreadLocal을 이용한 로그 추적기
- V3 ThreadLocal 버전
  - ThreadLocal을 이용하여 로그 추적기에 대한 동시성 이슈를 해결한다.
  - V3의 코드에서 [ThreadLocalLogTrace](./src/main/java/com/binghe/springcorethreadlocal/trace/logtrace/ThreadLocalLogTrace.java)만 구현하여 빈의 방향만 바꿔주면 된다.
  - 빈의 방향을 바꾸는 것은 [여기](./src/main/java/com/binghe/springcorethreadlocal/SpringCoreThreadlocalApplication.java)를 참고.

<br>

### ThreadLocal 주의사항
ThreadLocal과 ThreadPool을 같이 사용하면 주의해야한다.

ThreadPool 자체가 미리 Thread를 만들고 재활용하는 것이기 때문이다. 

만약 제대로 반환하지않으면 ThreadPool에 존재하는 Thread를 재활용할 때 동시성 이슈가 발생할 수 있다.

**이를 예방하기 위해선 `ThreadLocal.remove()`를 통해서 사용하고나서 제거해주는 작업을 꼭 해줘어야한다.**

