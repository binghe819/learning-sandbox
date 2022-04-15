package com.binghe.springbootjpaexample1.shoppin_mall.repository;

import com.binghe.springbootjpaexample1.shoppin_mall.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Member member) {
        entityManager.persist(member);
    }

    public Member findById(Long id) {
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll() {
        return entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findAllByName(String name) {
        return entityManager.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
