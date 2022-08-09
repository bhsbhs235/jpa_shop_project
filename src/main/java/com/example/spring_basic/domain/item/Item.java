package com.example.spring_basic.domain.item;

import com.example.spring_basic.domain.Category;
import com.example.spring_basic.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 한 테이블에 다 때려박는거
@DiscriminatorColumn(name = "dtype") // 자식 엔티티 구분자
//@Inheritance(strategy = InheritanceType.JOINED) // 제일 많이 쓰는거 Item 을 PK를 외래키로 갖고 자식 테이블을 생성
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 몰라도됨
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    // 엔티티 자체가 해결(비지니스 로직)할 수 있는 건 엔티티에서 해결하는 것이 객체지향적이다
    // 밖에서 재고수량을 측정한 후 setStockQuantity로 하는 것 보다 엔티티에서 해결하는 것이 객체지향적이다.
    /**
     * 재고(stock) 증가
     * @param quantity
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고(stock) 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
