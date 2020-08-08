package com.salhe.summer.antibigdata.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

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
    private String source;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
    private String platform;
    private String deviceId;

}
