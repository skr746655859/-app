package com.salhe.antibigdata.module

import android.content.Context
import androidx.room.Room
import com.salhe.antibigdata.dao.ProductsDao
import com.salhe.antibigdata.database.ProductsDatabase
import com.salhe.antibigdata.utils.SnowFlake
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideProductsDatabase(
        @ApplicationContext context: Context
    ): ProductsDatabase {
        return Room.databaseBuilder(
            context,
            ProductsDatabase::class.java,
            "products"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideProductsDao(
        productsDatabase: ProductsDatabase
    ): ProductsDao {
        return productsDatabase.productsDao()
    }

    @Provides
    fun provideSnowFlake() = SnowFlake(1, 1)

}