package com.salhe.antibigdata.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.salhe.antibigdata.PRODUCTS_TABLE_NAME

/**
 * wait 数据等待上传
 * uploaded 已上传
 * uploading 上传中
 * failed 上传失败
 */
enum class DataState {
    wait, uploaded, uploading, failed
}

class ProductConverters {

    @TypeConverter
    fun fromDataState(dataState: DataState?): String? {
        return dataState?.toString()
    }

    @TypeConverter
    fun toDataState(string: String?): DataState? {
        return if (string != null)
            DataState.valueOf(string)
        else null
    }

}

/**
 * 产品信息
 *
 * @property uid 记录的唯一标识符
 * @property name 商品名称
 * @property price 默认为商品固定价格
 * @property priceMax 商品最高价格（若此字段不为null，则认为price是价格下限）
 * @property priceBeforeDiscount 商品折前价格（若此字段不为null，则认为price是折后价）
 * @property state 数据上传状态
 */
@Entity(tableName = PRODUCTS_TABLE_NAME)
data class Product(
    @PrimaryKey val id: Long,
    val name: String,
    val price: Float,
    @ColumnInfo(name = "price_max") var priceMax: Float? = null,
    @ColumnInfo(name = "price_before_discount") var priceBeforeDiscount: Float? = null,
    val state: DataState = DataState.wait
) {

    fun withDiscount(priceBeforeDiscount: Float): Product {
        this.priceBeforeDiscount = priceBeforeDiscount
        return this
    }

    fun withRange(priceMax: Float?): Product {
        this.priceMax = priceMax
        return this
    }

}