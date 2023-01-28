# Thread Per Request Model을 사용하는 Spring MVC에서 API의 성능을 개선하기 - 동기/블로킹

## 배경
Thread Per Request Model을 사용하다보면 비교적 무거운 API가 탄생하기 마련이다.

예를 들어, 한 요청안에 외부 API 호출을 여러번한다던지, DB를 여러번 호출해야한다든지 등등.

이 프로젝트는 이러한 Thread Per Request Model에서 비교적 무거운 API의 성능을 개선하는 과정을 예제로 보여주기위해 만들어졌다.

V1은 동기/블로킹 방식으로 API 요청을 처리하는 예제이다.

V2는 비동기/블로킹 방식으로 API 요청을 처리하는 예제이다.
