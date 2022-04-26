package com.binghe.springbootjpaexample2.shoppin_mall.repository;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
