package com.salhe.summer.antibigdata.dao;

import com.salhe.summer.antibigdata.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsDao extends JpaRepository<Product, String> {

    Product findOneById(String id);

}
