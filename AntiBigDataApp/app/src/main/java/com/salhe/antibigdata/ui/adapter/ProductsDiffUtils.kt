package com.salhe.antibigdata.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.salhe.antibigdata.data.pojo.Product

class ProductsDiffUtils(
    private val oldProducts: List<Product>,
    private val newProducts: List<Product>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldProducts[oldItemPosition].id == newProducts[newItemPosition].id

    override fun getOldListSize() = oldProducts.size

    override fun getNewListSize() = newProducts.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldProducts[oldItemPosition] == newProducts[newItemPosition]

}