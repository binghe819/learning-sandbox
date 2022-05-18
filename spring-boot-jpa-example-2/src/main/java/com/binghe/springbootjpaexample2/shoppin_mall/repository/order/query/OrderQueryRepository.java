package com.binghe.springbootjpaexample2.shoppin_mall.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 목록 조회 1번, 컬렉션 N 번
     * 단건 조회에서 많이 사용되는 방식
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        // 루트 목록 조회 (ToOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders(); // 쿼리 1번

        // 루프를 돌면서 컬렉션 추가 (추가 쿼리 실행) 쿼리 N번
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    /**
     * 최적화 (컬렉션 최적화)
     * Query: 루트 목록 조회 1번, 컬렉션 1번
     * 데이터를 한꺼번에 처리할 때 많이 사용되는 방식
     */
    public List<OrderQueryDto> findOrderQueryDtos_optimization() {
        // 루트 목록 조회 (ToOne 코드를 모두 한번에 조회) - 쿼리 1번
        List<OrderQueryDto> result = findOrders();

        // OrderItem 컬렉션을 Map으로 한번에 조회 - 쿼리 1번
        List<Long> orderIds = result.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(toList());
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new com.binghe.springbootjpaexample2.shoppin_mall.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" + // ToOne 관계이기때문에 데이터 뻥튀기가 발생하지 않음으로 join해줌.
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class
        ).setParameter("orderIds", orderIds).getResultList();
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(groupingBy(OrderItemQueryDto::getOrderId));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    /**
     * 쿼리 한번에 가져오은 플랫 데이터 최적화
     * JOIN을 사용해서 모든 데이터를 하나의 쿼리로 모두 가져오는 것.
     */
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new com.binghe.springbootjpaexample2.shoppin_mall.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i",
                OrderFlatDto.class
        ).getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new com.binghe.springbootjpaexample2.shoppin_mall.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" + // ToOne 관계이기때문에 데이터 뻥튀기가 발생하지 않음으로 join해줌.
                        " join o.delivery d", // ToOne 관계이기때문에 데이터 뻥튀기가 발생하지 않음으로 join해줌.
                        OrderQueryDto.class)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new com.binghe.springbootjpaexample2.shoppin_mall.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" + // ToOne 관계이기때문에 데이터 뻥튀기가 발생하지 않음으로 join해줌.
                        " where oi.order.id = :orderId", OrderItemQueryDto.class
        ).setParameter("orderId", orderId).getResultList();
    }

}
