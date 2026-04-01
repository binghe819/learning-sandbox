package com.binghe.service;

import com.binghe.domain.Item;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Long save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCondition itemSearch);
}
