package com.binghe.repository;

import com.binghe.domain.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {

    void save(Item item);

    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto itemUpdateDto);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCondition itemSearch);
}
