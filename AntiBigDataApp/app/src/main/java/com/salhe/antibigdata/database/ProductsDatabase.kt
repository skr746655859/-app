package com.salhe.antibigdata.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.salhe.antibigdata.dao.ProductsDao
import com.salhe.antibigdata.pojo.Product
import com.salhe.antibigdata.pojo.ProductConverters

@Database(entities = arrayOf(Product::class), version = 1)
@TypeConverters(ProductConverters::class)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

}