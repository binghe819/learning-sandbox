package com.binghe.springbootjpaexample2.shoppin_mall.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
