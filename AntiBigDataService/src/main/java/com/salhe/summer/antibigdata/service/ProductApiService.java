package com.salhe.summer.antibigdata.service;

import com.salhe.summer.antibigdata.dto.ApiResult;
import com.salhe.summer.antibigdata.pojo.Product;

public interface ProductApiService {

    ApiResult addProduct(Product product);

    ApiResult getProduct(String productId);

    ApiResult listProducts(int page, int size);
}
