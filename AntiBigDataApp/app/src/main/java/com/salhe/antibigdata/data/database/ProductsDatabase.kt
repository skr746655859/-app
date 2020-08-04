package com.salhe.antibigdata.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.salhe.antibigdata.data.dao.ProductsDao
import com.salhe.antibigdata.data.pojo.Product
import com.salhe.antibigdata.data.pojo.ProductConverters

@Database(entities = arrayOf(Product::class), version = 1)
@TypeConverters(ProductConverters::class)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

}