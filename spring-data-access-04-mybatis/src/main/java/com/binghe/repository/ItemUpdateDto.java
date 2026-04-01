package com.binghe.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemUpdateDto {

    private String itemName;
    private Integer price;
    private Integer quantity;
}
