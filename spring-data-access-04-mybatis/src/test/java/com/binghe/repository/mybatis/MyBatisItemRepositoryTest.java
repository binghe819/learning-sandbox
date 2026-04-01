package com.binghe.repository.mybatis;

import com.binghe.config.MyBatisConfig;
import com.binghe.domain.Item;
import com.binghe.repository.ItemRepository;
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
import static org.assertj.core.api.Assertions.tuple;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MyBatisConfig.class)
class MyBatisItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void save() {
        Item item = new Item(null, "itemA", 1000, 10);

        Long savedId = itemRepository.save(item);

        Optional<Item> found = itemRepository.findById(savedId);
        assertThat(found).isPresent();
        assertThat(found.get())
                .extracting(Item::getItemName, Item::getPrice, Item::getQuantity)
                .containsExactly("itemA", 1000, 10);
    }

    @Test
    void update() {
        Long savedId = itemRepository.save(new Item(null, "itemA", 1000, 10));

        itemRepository.update(savedId, new ItemUpdateDto("itemB", 2000, 20));

        Item updated = itemRepository.findById(savedId).orElseThrow();
        assertThat(updated)
                .extracting(Item::getItemName, Item::getPrice, Item::getQuantity)
                .containsExactly("itemB", 2000, 20);
    }

    @Test
    void findById_found() {
        Long savedId = itemRepository.save(new Item(null, "itemA", 1000, 10));

        Optional<Item> found = itemRepository.findById(savedId);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedId);
    }

    @Test
    void findById_notFound() {
        Optional<Item> found = itemRepository.findById(Long.MAX_VALUE);

        assertThat(found).isEmpty();
    }

    @Test
    void findAll_noCondition() {
        itemRepository.save(new Item(null, "itemA", 1000, 10));
        itemRepository.save(new Item(null, "itemB", 2000, 20));

        List<Item> items = itemRepository.findAll(new ItemSearchCondition(null, null));

        assertThat(items)
                .extracting(Item::getItemName)
                .contains("itemA", "itemB");
    }

    @Test
    void findAll_byItemName() {
        itemRepository.save(new Item(null, "itemA", 1000, 10));
        itemRepository.save(new Item(null, "itemB", 2000, 20));
        itemRepository.save(new Item(null, "special", 500, 5));

        List<Item> items = itemRepository.findAll(new ItemSearchCondition("item", null));

        assertThat(items)
                .extracting(Item::getItemName)
                .contains("itemA", "itemB")
                .doesNotContain("special");
    }

    @Test
    void findAll_byMaxPrice() {
        itemRepository.save(new Item(null, "cheap", 500, 10));
        itemRepository.save(new Item(null, "expensive", 5000, 10));

        List<Item> items = itemRepository.findAll(new ItemSearchCondition(null, 1000));

        assertThat(items)
                .extracting(Item::getItemName)
                .contains("cheap")
                .doesNotContain("expensive");
    }

    @Test
    void findAll_byItemNameAndMaxPrice() {
        itemRepository.save(new Item(null, "itemA", 1000, 10));
        itemRepository.save(new Item(null, "itemB", 5000, 20));
        itemRepository.save(new Item(null, "other", 500, 5));

        List<Item> items = itemRepository.findAll(new ItemSearchCondition("item", 2000));

        assertThat(items)
                .extracting(Item::getItemName, Item::getPrice)
                .contains(tuple("itemA", 1000))
                .doesNotContain(tuple("itemB", 5000), tuple("other", 500));
    }
}
