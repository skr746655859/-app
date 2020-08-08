package com.salhe.antibigdata.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.salhe.antibigdata.R
import com.salhe.antibigdata.data.pojo.DataState
import com.salhe.antibigdata.data.pojo.Product
import kotlinx.android.synthetic.main.item_product.view.*
import java.text.SimpleDateFormat
import java.util.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.InnerHolder>() {

    private val products = mutableListOf<Product>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)

    inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InnerHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val product = products[position]
        holder.itemView.apply {
            nameTextView.text = product.name
            stateTextView.text = when (product.state) {
                DataState.wait -> "未上传"
                DataState.uploading -> "正在上传"
                DataState.uploaded -> "上传成功"
                DataState.failed -> "上传失败"
            }

            if (product.priceMax != null) {
                priceTextView.text = "价格：${product.price} ~ ${product.priceMax}"
            } else if (product.priceBeforeDiscount != null) {
                priceTextView.text = "现价：${product.price}，原价${product.priceBeforeDiscount}"
            } else {
                priceTextView.text = "固定价：${product.price}"
            }

            sourceTextView.text = product.source
            urlTextView.text = if (product.url.isNullOrEmpty()) "NO URL" else product.url
            timeTextView.text = dateFormat.format(product.time)

        }
    }

    fun setAllProducts(products: List<Product>) {
        val diffResult = DiffUtil.calculateDiff(ProductsDiffUtils(this.products, products))
        diffResult.dispatchUpdatesTo(this)
        this.products.clear()
        this.products.addAll(products)
    }
}