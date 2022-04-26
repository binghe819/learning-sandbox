package com.binghe.springbootjpaexample2.shoppin_mall.repository;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class ItemRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public void save(Item item) {
        if (Objects.isNull(item.getId())) {
            entityManager.persist(item);
        }
        entityManager.merge(item); // update와 같은 역할 (item을 영속화하지 않고, 새로운 item을 만들어 영속화한다음 반환한다.)
//        Item merge = entityManager.merge(item);
    }

    public Item findById(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
