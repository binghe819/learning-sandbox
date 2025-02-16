# 스프링 테스트 격리 테스트

# API 호출 curl

> 저장

```shell
curl -X POST http://localhost:8080/members \
     -H "Content-Type: application/json" \
     -d '{
           "name": "binghe",
           "address": "binghe address",
           "description": "hihihihihihi~"
         }'
```

> 조회 (단건)

```shell
curl -X GET http://localhost:8080/members/1
```

> 조회 (다건)

```shell
curl -X GET http://localhost:8080/members
```

> 조회 (개수)

```shell
curl -X GET http://localhost:8080/members/count
```

> 업데이트

```shell

curl -X PUT http://localhost:8080/members \
     -H "Content-Type: application/json" \
     -d '{
           "id": 1,
           "name": "Binghe Updated",
           "address": "Binghe address Updated",
           "description": "Binghe description"
         }'
```

> 전체삭제

```shell
curl -X DELETE http://localhost:8080/members
```
