package com.salhe.antibigdata.service

import com.salhe.antibigdata.data.pojo.Product
import retrofit2.Call
import retrofit2.http.*

class ApiResult {

    companion object {
        const val SUCCESS = 0
        const val ERROR = 1
        const val BAD_REQUEST = 400
        const val PERMISSION_DENIED = 403
        const val NOT_FOUND = 404
        const val METHOD_NOT_ALLOWED = 405
        const val INTERNAL_SERVER_ERROR = 500
    }

    data class StringResult(
        val code: Int,
        val msg: String,
        val data: String?
    )

    data class ProductResult(
        val code: Int,
        val msg: String,
        val data: Product?
    )

}

interface ProductRemoteService {

    @PUT("api/product/new_one")
    fun addProduct(@Body product: Product): Call<ApiResult.ProductResult>

    @GET("api/product/{product_id}")
    fun getProduct(@Path("product_id") productId: Long): Call<ApiResult.ProductResult>

    @GET("api/product/list")
    fun listProducts(
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): Call<ApiResult.StringResult>

}