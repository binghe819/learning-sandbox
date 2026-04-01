package com.binghe.repository;

import com.binghe.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Long save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCondition cond);
}
