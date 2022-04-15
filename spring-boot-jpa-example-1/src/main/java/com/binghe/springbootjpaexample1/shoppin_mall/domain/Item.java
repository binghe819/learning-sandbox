package com.binghe.springbootjpaexample1.shoppin_mall.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속 전략
@DiscriminatorColumn(name = "dtype") // 싱글 테이블 전략에서 사용될 상태 이름
public abstract class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    private int price;

    private int stockQuantity;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        if (this.stockQuantity - quantity < 0) {
            throw new IllegalStateException("재고는 0보다 작을 수 없다.");
        }
        this.stockQuantity -= quantity;
    }
}
