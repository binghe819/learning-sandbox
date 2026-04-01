package com.binghe.service;

import com.binghe.config.MyBatisConfig;
import com.binghe.domain.Item;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MyBatisConfig.class)
class ItemServiceV2Test {

    @Autowired
    ItemService itemService;

    @Test
    void save() {
        Long savedId = itemService.save(new Item(null, "itemA", 1000, 10));

        Optional<Item> found = itemService.findById(savedId);
        assertThat(found).isPresent();
        assertThat(found.get())
                .extracting(Item::getItemName, Item::getPrice, Item::getQuantity)
                .containsExactly("itemA", 1000, 10);
    }

    @Test
    void update() {
        Long savedId = itemService.save(new Item(null, "itemA", 1000, 10));

        itemService.update(savedId, new ItemUpdateDto("itemB", 2000, 20));

        Item updated = itemService.findById(savedId).orElseThrow();
        assertThat(updated)
                .extracting(Item::getItemName, Item::getPrice, Item::getQuantity)
                .containsExactly("itemB", 2000, 20);
    }

    @Test
    void findById_notFound() {
        Optional<Item> found = itemService.findById(Long.MAX_VALUE);

        assertThat(found).isEmpty();
    }

    @Test
    void findItems_noCondition() {
        itemService.save(new Item(null, "itemA", 1000, 10));
        itemService.save(new Item(null, "itemB", 2000, 20));

        List<Item> items = itemService.findItems(new ItemSearchCondition(null, null));

        assertThat(items)
                .extracting(Item::getItemName)
                .contains("itemA", "itemB");
    }

    @Test
    void findItems_byItemNameAndMaxPrice() {
        itemService.save(new Item(null, "itemA", 1000, 10));
        itemService.save(new Item(null, "itemB", 5000, 20));

        List<Item> items = itemService.findItems(new ItemSearchCondition("item", 2000));

        assertThat(items)
                .extracting(Item::getItemName)
                .contains("itemA")
                .doesNotContain("itemB");
    }
}
