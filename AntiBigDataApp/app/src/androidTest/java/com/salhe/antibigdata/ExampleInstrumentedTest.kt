package com.salhe.antibigdata

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.salhe.antibigdata.database.ProductsDatabase
import com.salhe.antibigdata.pojo.Product
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.salhe.antibigdata", appContext.packageName)
    }

    @Test
    suspend fun insertTestData() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val productsDao = Room.databaseBuilder(
            appContext,
            ProductsDatabase::class.java,
            PRODUCTS_TABLE_NAME
        ).build()
            .productsDao()

        for (i in 1..1000) {
            productsDao.insertAll(
                Product(
                    i.toLong(),
                    "商品$i",
                    Random.nextFloat()
                )
            )
        }
    }
}