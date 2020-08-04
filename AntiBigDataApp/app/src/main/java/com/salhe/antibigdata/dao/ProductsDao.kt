package com.salhe.antibigdata.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.salhe.antibigdata.PRODUCTS_TABLE_NAME
import com.salhe.antibigdata.pojo.Product

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg products: Product)

    @Query("SELECT * FROM $PRODUCTS_TABLE_NAME")
    fun loadAllProductsLiveData(): LiveData<List<Product>>

    @Query("SELECT * FROM $PRODUCTS_TABLE_NAME LIMIT :row-1,1")
    fun loadProductByRowLiveData(row: Int): LiveData<Product>

    @Query("SELECT * FROM $PRODUCTS_TABLE_NAME WHERE id = :id")
    fun loadProductByIdLiveData(id: Long): LiveData<Product>

    @Query("SELECT * FROM $PRODUCTS_TABLE_NAME WHERE id = :id")
    fun loadProductById(id: Long): Product

    @Query("SELECT COUNT(*) FROM $PRODUCTS_TABLE_NAME")
    fun countProducts(): Int

}