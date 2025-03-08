package com.binghe.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 실제 도메인 (엔티티)의 경우는 final로 만드는게 좋으나 테스트이므로 모두 DTO성으로 구성.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Member {

    private Long id;

    private String name;

    private String address;

    private String description;

    public Member(String name, String address, String description) {
        this(null, name, address, description);
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
