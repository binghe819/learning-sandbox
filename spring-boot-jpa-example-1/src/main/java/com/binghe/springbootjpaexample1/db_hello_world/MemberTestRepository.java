package com.binghe.springbootjpaexample1.db_hello_world;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberTestRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(MemberTest memberTest) {
        em.persist(memberTest);
        return memberTest.getId();
    }

    public MemberTest find(Long id) {
        return em.find(MemberTest.class, id);
    }
}
