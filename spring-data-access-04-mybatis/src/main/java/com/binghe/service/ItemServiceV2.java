package com.binghe.service;

import com.binghe.domain.Item;
import com.binghe.repository.ItemRepository;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis 사용하는 ItemServiceV2
 */
public class ItemServiceV2 implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceV2(ItemRepository itemRepository) {
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
    public List<Item> findItems(ItemSearchCondition itemSearch) {
        return itemRepository.findAll(itemSearch);
    }
}
