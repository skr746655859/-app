package com.salhe.antibigdata.data.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.salhe.antibigdata.PRODUCTS_TABLE_NAME
import java.util.*

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

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(time: Long): Date {
        return Date(time)
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
 * @property source 数据来源，比如taobao, taobao-web, jd, jd-web
 * @property url 相关URL
 * @property time 数据参考时间
 * @property platform 操作系统，如android，ios，macOS，Windows
 * @property deviceId 设备ID
 * @property state 数据上传状态
 */
@Entity(tableName = PRODUCTS_TABLE_NAME)
data class Product(
    @PrimaryKey val id: Long,
    val name: String,
    val price: Float,
    @ColumnInfo(name = "price_max")
    val priceMax: Float? = null,
    @ColumnInfo(name = "price_before_discount")
    val priceBeforeDiscount: Float? = null,
    val source: String = "",
    val url: String? = null,
    val time: Date = Date(),
    val platform: String = "android",
    @ColumnInfo(name="device_id")
    val deviceId: String = "",
    val state: DataState = DataState.wait
)