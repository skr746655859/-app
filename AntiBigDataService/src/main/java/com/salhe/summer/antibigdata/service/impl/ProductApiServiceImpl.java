package com.salhe.summer.antibigdata.service.impl;

import com.salhe.summer.antibigdata.dao.ProductsDao;
import com.salhe.summer.antibigdata.dto.ApiResult;
import com.salhe.summer.antibigdata.pojo.Product;
import com.salhe.summer.antibigdata.service.ProductApiService;
import com.salhe.summer.antibigdata.utils.SnowFlake;
import com.salhe.summer.antibigdata.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.rmi.server.ExportException;

@Service
public class ProductApiServiceImpl implements ProductApiService {

    @Autowired
    private SnowFlake snowFlake;

    @Resource
    private ProductsDao productsDao;

    @Override
    public ApiResult addProduct(Product product) {
        product.setId(snowFlake.nextId() + "");
        if (product.getPrice() < 0)
            return ApiResult.error().msg("价格有误");
        if (TextUtils.isEmpty(product.getName()))
            return ApiResult.error().msg("商品名称不能为空");
        if (TextUtils.isEmpty(product.getSource()))
            return ApiResult.error().msg("商品来源不可为空");
        if (product.getTime() == null)
            return ApiResult.error().msg("时间不可为空");
        if (TextUtils.isEmpty(product.getPlatform()))
            return ApiResult.error().msg("操作系统平台不可为空");
        if (TextUtils.isEmpty(product.getDeviceId()))
            return ApiResult.error().msg("设备ID不可为空");

        productsDao.save(product);
        return ApiResult.success().msg("添加成功").data(product);
    }

    @Override
    public ApiResult getProduct(String productId) {
        try {
            Product product = productsDao.findOneById(productId);
            return ApiResult.success().data(product);
        } catch (Exception e) {
            return ApiResult.error().msg("不存在该商品");
        }
        // return null;
    }

    @Override
    public ApiResult listProducts(int page, int size) {
        if (page <= 0)
            return ApiResult.error().msg("页码错误");
        if (size <= 0)
            return ApiResult.error().msg("size错误");

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> products = productsDao.findAll(pageable);
        return ApiResult.success().data(products.toList());
    }

}
