package com.salhe.summer.antibigdata.pojo;

import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "products_db")
public class Product {

    @Id
    private String id;
    private String name;
    private float price;
    @Column(name = "price_max")
    private Float priceMax;
    @Column(name = "price_before_discount")
    private Float priceBeforeDiscount;

}
