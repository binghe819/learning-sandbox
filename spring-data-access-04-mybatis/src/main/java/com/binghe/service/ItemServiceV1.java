package com.binghe.service;

import com.binghe.domain.Item;
import com.binghe.repository.ItemRepository;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate을 이용한 ItemService
 */
public class ItemServiceV1 implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceV1(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Long save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemRepository.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCondition cond) {
        return itemRepository.findAll(cond);
    }
}
