package com.salhe.summer.antibigdata.controller;

import com.salhe.summer.antibigdata.dto.ApiResult;
import com.salhe.summer.antibigdata.pojo.Product;
import com.salhe.summer.antibigdata.service.ProductApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    @Autowired
    private ProductApiService productApiService;

    @PutMapping("/new_one")
    public ApiResult addProduct(@RequestBody Product product) {
        return productApiService.addProduct(product);
    }

    @GetMapping("/{product_id}")
    public ApiResult getProduct(@PathVariable("product_id") String productId){
        return productApiService.getProduct(productId);
    }

    @GetMapping("/list")
    public ApiResult listProducts(@RequestParam int page,
                                  @RequestParam int size){
        return productApiService.listProducts(page, size);
    }

}
