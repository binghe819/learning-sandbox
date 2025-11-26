# Apache Common Object Pooling

<br>

# Object Pooling 이란?

<br>

💁‍♂️ **Object Pooling 이란? 왜 사용하는가?**

* Object Pooling이란?
  * Object Pooling은 자주 사용되는 객체를 미리 생성하고 재사용하여 메모리 할당 및 해제의 오버헤드를 줄이는 기술이다.
* 왜 사용하는가?
  * Java와 같은 언어에서는 객체를 빈번하게 생성하고 폐기하는 작업이 성능에 큰 영향을 미칠 수 있다.
  * 특히 복잡한 객체나 자주 호출되는 객체가 있는 경우, 이러한 작업은 GC(Garbage Collection)에 많은 부하를 줄 수 있으며, 이는 전체 시스템 성능에 악영향을 미치게 된다.
  * 이때 Object pooling을 사용하면 자주 사용하는 객체들을 미리 만들어 두고 필요할 때 재활용할 수 있다. 
  * 객체를 새로 생성하는 대신, 풀(pool)에서 기존 객체를 가져와 사용한 후 다시 반납하는 방식으로 동작한다. 이를 통해 메모리 할당 비용을 줄이고, 성능을 크게 향상시킬 수 있다.

<br>

💁‍♂️ **Object Pooling 직접 구현한다면**

```java
import java.util.function.Supplier;public class ObjectPool<T> {
    private Queue<T> pool = new LinkedList<>();
    private int maxSize;

    public ObjectPool(int maxSize) {
        this.maxSize = maxSize;
        this.pool = init(Supplier<T> creator); // 초기에 미리 객체들 생성해서 풀에 저장해두는 로직.
    }

    public synchronized T borrowObject(Supplier<T> creator) {
        if (pool.isEmpty()) {
            return creator.get();
        }
        return pool.poll();
    }

    public synchronized void returnObject(T obj) {
        if (pool.size() < maxSize) {
            pool.offer(obj);
        }
    }
}
``` 

* 코드가 직관적이기 때문에 알아보기 쉬울 것이며, borrowObject() 메서드를 사용해 객체를 풀에서 가져오고, returnObject()로 객체를 반납하는 방식으로 작동한다.
* borrowObject()와 returnObject()에서는 synchronized를 사용하여 여러 스레드가 동시에 객체 풀에 접근할 때의 안전성을 보장해야 한다.
* 이 방법은 간단한 경우에 유용한 방법이지만, 객체 생명주기 관리 및 동기화 등 여러 복잡한 부분은 추가로 고려해야 하기 때문에 권장되는 방식은 아니며 되도록이면 아래 설명하는 라이브러릴 사용하도록 하자.

<br>

# Apache Commons Pool2 라이브러리

Apache Commons Pool 은 오픈소스 소프트웨어로서 객체 풀링 API 와 여러 객체풀 구현들을 제공한다. 

Apache Commons Pool 버전 2 는 1.x 버전대비 완전히 다시 코딩된 풀링 구현을 포함하고 있다. 

버전 2는 퍼포먼스와 확장성 향상과 함께 인스턴스 트래킹과 풀 모니터링을 포함하고, JDK 버전 1.6 이상에서 동작한다.

<br>

## PooledObjectFactory<T>

PooledObjectFactory는 ObjectPool(예: GenericObjectPool)이 객체를 만들고 쓰고 반납할 때 호출하는 라이프사이클 **콜백 인터페이스**이다.

> 즉, ObjectPool에 이 인터페이스의 구현체를 개발자가 구현하여 넘겨주면 풀링시 콜백받는 인터페이스.

핵심 메서드는 아래와 같다.

```java
public interface PooledObjectFactory<T> {
    PooledObject<T> makeObject() throws Exception;        // 새 객체 생성 & 래핑
    void destroyObject(PooledObject<T> p) throws Exception; // 파괴
    boolean validateObject(PooledObject<T> p);           // 아직 쓸 수 있는지 검증
    void activateObject(PooledObject<T> p) throws Exception;  // 빌려갈 때
    void passivateObject(PooledObject<T> p) throws Exception; // 반납할 때
}
```
* `makeObject`: 풀에 넣을 객체를 새로 생성해서 PooledObject에 감싸서 반환
* `destroyObject`: 더 이상 안 쓸 객체를 완전히 정리/종료
* `validateObject`: 빌려주기 전에 여전히 유효한지 체크 (연결 끊겼나, 상태 손상됐나 등)
* `activateObject`: borrowObject()로 풀에서 꺼낼 때, 초기화/재설정
* `passivateObject`: returnObject()로 풀에 반납할 때, 다음 사용을 위해 상태 정리

위와 같이, “풀에 담긴 객체의 전 생애를 관리하는 계약(Contract)”만 정의한 것이며 구현은 전부 사용자가 해야 한다.

**이 인터페이스를 직접 구현하려면 위에 나열된 모든 메서드를 명시적으로 구현해야 합니다. 이는 구현할 메서드가 많고 번거로울 수 있다. 그래서 보통 아래 `BasePooledObjectFactory`를 사용한다.**

<br>

## BasePooledObjectFactory<T>

`PooledObjectFactory`를 구현해 둔 추상 클래스이며, Factory 구현을 쉽게 하기 위해 제공된다.

주요 특징은 아래와 같다.

* `PooledObjectFactory` 인터페이스의 5가지 메서드 중 `makeObject()`와 `wrap()` 메서드를 제외한 나머지 (`destroyObject`, `validateObject`, `activateObject`, `passivateObject`)에 대해 기본(No-op) 구현을 제공한다.
* `makeObject()`는 추상 메서드로 남겨두어, 사용자가 반드시 풀링할 객체를 생성하는 로직만 구현하도록 강제한다.
* 객체를 `PooledObject`로 감싸는 `wrap()` 메서드도 추상으로 남겨둔다.

즉, 필수로 구현해야하는 메서드는 아래 두 가지이다.

* `create()`
  * 풀에 저장할 새로운 객체 T 자체를 생성한다. (`makeObject()`의 일부)
* `wrap(T obj)`
  * 생성된 객체 T를 `PooledObject<T>`로 감싸서 반환한다.

<br>

## PooledObjectFactory와 BasePooledObjectFactory의 차이점

`PooledObjectFactory`는 ObjectPool 사용시 따라야 하는 인터페이스(약속)이며, `BasePooledObjectFactory`는 이 인터페이스를 상속받아 일반적인 경우에 쉽게 구현할 수 있도록 돕는 편의 클래스라고 볼 수 있다.
 
대부분의 Pool2 사용자는 `BasePooledObjectFactory`를 상속받아 사용하는 것이 가장 간단하고 효율적이다.

<br>

## GenericObjectPool

`GenericObjectPool<T>`는 가장 일반적으로 사용되는 객체 풀(Object Pool) 구현체이다. 객체 생성 비용이 크거나, 제한된 리소스를 재사용해야 할 때 사용되며, `PooledObjectFactory<T>` 또는 `BasePooledObjectFactory<T>`와 함께 동작한다.

<br>

💁‍♂️ **GenericObjectPool이란?**

```java
GenericObjectPool<MyClient> pool = new GenericObjectPool<>(new MyClientFactory());
MyClient client = pool.borrowObject();   // 객체 꺼내기
pool.returnObject(client);              // 다시 반납
```

* “객체를 미리 만들어 저장해두고, 필요할 때 빌려주고(Return하면 재사용) 관리해주는 풀 구현체”
* `ObjectPool<T>` 인터페이스 구현체
* 단일 스레드뿐만 아니라 멀티스레드 환경에서도 안전하게 사용 가능\
* 동적 풀 크기 조절, 유휴 객체 자동 제거, 검증 및 활성화(passivation/activation) 기능 제공
* `GenericKeyedObjectPool`도 존재하지만, 대부분의 경우 `GenericObjectPool`이 사용됨

<br>

💁‍♂️ **주요 메서드**

| 메서드                                  | 역할               | 예외 발생          |
| ------------------------------------ | ---------------- | -------------- |
| `borrowObject()`                     | 풀에서 객체 빌려오기      | 풀 고갈 시 maxWaitMillis 만큼 대기 그래도 없으면 예외 발생 |
| `returnObject(T obj)`                | 객체 반납            | 없음             |
| `invalidateObject(T obj)`            | 객체에 문제가 생겼을 때, 반환 대신 폐기     | ✔              |
| `addObject()`                        | 미리 객체 생성하여 풀에 넣기 (Warm-up 용도) | 가능             |
| `getNumIdle()`                       | 현재 대기(idle) 객체 수 | X              |
| `getNumActive()`                     | 현재 사용 중 객체 수     | X              |
| `clear()`                            | 모든 idle 객체 삭제    | ✔              |
| `close()`                            | 풀 완전 종료          | ✔              |
| `setConfig(GenericObjectPoolConfig)` | 설정 변경            | X              |


<br>

💁‍♂️ **ObjectPool내 Object의 생명주기**

```scss
┌─────────────┐
│ create()    │ ← factory.makeObject()
└─────┬───────┘
      │ wrap()
┌─────▼───────┐
│a) idle 상태 │
└─────┬───────┘
      │ borrowObject()
┌─────▼───────┐
│b) activate  │
│   validate  │
└─────┬───────┘
      │ 사용
      │ returnObject()
┌─────▼───────┐
│c) passivate │
│d) 다시 idle │
└─────────────┘
```

<br>

💁‍♂️ **주요 설정**

| 설정값                             | 설명                       | 기본값        |
| ------------------------------- | ------------------------ | ---------- |
| `maxTotal`                      | **풀에서 생성 가능한 총 객체 수 제한** | 8          |
| `maxIdle`                       | Idle 상태에서 보관 가능한 최대 객체 수 | 8          |
| `minIdle`                       | 유지할 최소 Idle 객체 수         | 0          |
| `maxWaitMillis`                 | 객체 부족 시 대기 시간            | -1 (무한 대기) |
| `testWhileIdle`                 | Idle 대상 검증 여부            | false      |
| `testOnBorrow`                  | `borrowObject()` 시 검사    | false      |
| `testOnReturn`                  | `returnObject()` 시 검사    | false      |
| `timeBetweenEvictionRunsMillis` | Idle 객체 정리 주기            | -1         |
| `minEvictableIdleTimeMillis`    | Idle 상태에서 제거까지 대기 시간     | 30분        |
| `blockWhenExhausted`            | 풀 고갈 시 예외 vs. 대기         | true       |

<br>

일반적으로 성능 & 안정성 균형 위해 아래 설정처럼 많이 사용된다.

```java
GenericObjectPoolConfig<MyClient> config = new GenericObjectPoolConfig<>();
config.setMaxTotal(50);
config.setMaxIdle(20);
config.setMinIdle(5);
config.setTestOnBorrow(true);
config.setTestWhileIdle(true);
config.setMaxWaitMillis(3000);

GenericObjectPool<MyClient> pool = new GenericObjectPool<>(new MyClientFactory(), config);
```

<br>

💁‍♂️ **사용 예시와 사용시 주의할 점**

```java
GenericObjectPool<MyClient> pool =
        new GenericObjectPool<>(new MyClientFactory(), config);

MyClient client = null;
try {
    client = pool.borrowObject();         // ① 풀에서 객체 가져오기
    client.callApi();                     // ② 사용할 로직 실행
} catch (Exception e) {
    pool.invalidateObject(client);        // ③ 오류 시 폐기
} finally {
    if (client != null) {
        pool.returnObject(client);        // ④ 정상은 반납
    }
}
```

* `borrowObject()` 후 반드시 `returnObject()` or `invalidateObject()` 해야 함
  * 안 하면 메모리 누수 & 풀 고갈 위험
* `validateObject()`를 꼭 구현하거나 `testOnBorrow=true`
  * 깨진 객체가 풀에서 재사용되면 오류 발생
* `destroyObject()`에서 close/cleanup 로직 넣기
* `pool.close()` 호출 시 전체 종료(스레드 포함)






