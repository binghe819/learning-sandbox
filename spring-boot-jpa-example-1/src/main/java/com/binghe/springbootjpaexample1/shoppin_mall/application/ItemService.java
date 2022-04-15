package com.binghe.springbootjpaexample1.shoppin_mall.application;

import com.binghe.springbootjpaexample1.shoppin_mall.domain.Item;
import com.binghe.springbootjpaexample1.shoppin_mall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}
