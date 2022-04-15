package com.binghe.springbootjpaexample1.shoppin_mall.repository;

import com.binghe.springbootjpaexample1.shoppin_mall.domain.Member;
import com.binghe.springbootjpaexample1.shoppin_mall.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order findbyId(Long id) {
        return entityManager.find(Order.class, id);
    }

    /**
     * 동적 쿼리를 어떻게 작성할 것인가?
     */
    public List<Order> findByOrderSearch(OrderSearch orderSearch) {
        return entityManager.createQuery("select o from Order o where o.status = :status and o.member.name = :member", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("member", orderSearch.getMemberName())
                .getResultList();
    }

    /**
     * JPQL로 동적 쿼리 처리하는 예시 (Bad Practice) - 조건에 따라 String문을 조립하는 방식.
     */
    public List<Order> findByOrderSearchBadPractice(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m"; // join을 굳이 안해도 상관없음
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
                .setMaxResults(1000); // 최대 1000건 조회

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    /**
     * JPA Criteria로 동적쿼리 처리하는 예시 (Bad Practice) -
     */
    public List<Order> findByOrderSearchBadPractice2(OrderSearch orderSearch) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = entityManager.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }
}
