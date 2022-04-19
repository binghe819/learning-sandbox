package com.binghe.springbootjpaexample1.shoppin_mall.presentation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class Memberform {

    @NotEmpty(message = "회원 이름은 필수")
    private String name;

    private String city;

    private String street;

    private String zipcode;
}
