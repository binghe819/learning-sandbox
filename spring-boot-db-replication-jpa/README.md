# Spring Boot + JPA 기반의 DB Replication 학습 테스트
> 정리 내용은 [여기](https://github.com/binghe819/TIL/blob/master/Spring/DB/Replication/Replication%20With%20JPA.md) 참고.

<br>

테스트시 [application.yml](./src/main/resources/application.yml)에서 DataSource 설정 필요. 

<br>

## 구현 순서
- `DataSourceProperties`
    - Profile에 정의된 Master와 Slave의 정보를 객체에 바인딩하는 코드.
    - 주요 키워드
        - `@ConfigurationProperties(prefix = "datasource")`
        - `@EnableConfigurationProperties(DataSourceProperties.class)`
- `ReplicationRoutingDataSource`
    - 조회 쿼리시 Slave DataSource를 Round Robin방식으로 선택하는 코드.
    - 주요 키워드
        - `AbstractRoutingDataSource` 상속
- `ReplicationDataSourceConfiguration`
    - `ReplicationRoutingDataSource` 빈 등록 (`DataSourceProperties`내용 주입)
    - `LazyConnectionDataSourceProxy` 빈 등록
        - Spring은 트랜잭션에 진입하는 순간 이미 설정된 DataSource의 커넥션을 가져온다.
        - 매 트랜잭션마다 새로운 DataSource를 가져오도록 하기 위해 Lazy + Proxy로 된 DataSource를 이용한다.
    - Jpa 관련 빈 설정
        - JPA에서 사용할 TransactionManager 설정 (entityManagerFactory로 인해 정의해준다)
        - JPA에서 사용되는 EntityManagerFactory 설정 (LazyConnectionDataSourceProxy를 넣기 위함)

<br>

## 테스트 방법
1. `application.yml`에 Replication 설정이 완료된 Master DB와 Slave DB의 정보를 기입한다.
2. `DataSourcePropertiesTest`가 문제없이 동작한다면, `ReplicationUserServiceTEst`를 돌리고 로그를 통해 Replication이 제대로 동작하는지 확인한다.
