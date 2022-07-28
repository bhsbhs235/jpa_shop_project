package com.example.spring_basic.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    // @Enumerated(EnumType.ORDER) 숫자로 들어가면 나중이 중간에 추가되면 꼬인다
    private DeliveryStatus status;
}
