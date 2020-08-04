package com.salhe.antibigdata.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.salhe.antibigdata.Key
import com.salhe.antibigdata.R
import com.salhe.antibigdata.data.dao.ProductsDao
import com.salhe.antibigdata.data.pojo.DataState
import com.salhe.antibigdata.data.pojo.Product
import com.salhe.antibigdata.ui.adapter.ProductAdapter
import com.salhe.antibigdata.utils.SnowFlake
import com.salhe.antibigdata.work.UploadProductWork
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // DI
    @Inject
    lateinit var productsDao: ProductsDao

    @Inject
    lateinit var snowFlake: SnowFlake

    // Data
    lateinit var allProducts: LiveData<List<Product>>

    // View
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
        initObserver()

    }

    private fun initData() {
        allProducts = productsDao.loadAllProductsLiveData()
    }

    private fun initObserver() {
        allProducts.observe(this, Observer {
            countTextView.text = "共有 ${it.size} 条记录"
            productAdapter.setAllProducts(it)
        })
    }

    private fun initView() {
        productAdapter = ProductAdapter()
        productsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productsRecyclerView.adapter = productAdapter
    }

    fun addTestData(v: View) {
        productsDao.insertAll(
            // 固定价格商品
            Product(
                snowFlake.nextId(),
                "【一元拍卖】Huawei/华为Nova 7 SE 5G新品手机 8+128GB 银月星辉",
                2751.00f
            ),
            // 波动价格的商品
            Product(
                snowFlake.nextId(),
                "HUAWEI P40 Pro 5G新款手机官方旗舰店mate30pro5g版正品华为p40pro直降p30",
                4188.00f
            ).withRange(7388.00f),
            // 打折商品
            Product(
                snowFlake.nextId(),
                "休闲裤男长裤夏季薄款抽绳九分裤束脚宽松裤子韩版潮流速干运动裤",
                58.00f
            ).withDiscount(168.00f)
        )
    }

    fun uploadAllData(v: View) {
        val workManager = WorkManager.getInstance(this)
        allProducts.value?.forEach {
            if (it.state != DataState.uploaded) {
                val data = workDataOf(Key.ID to it.id)
                val request = OneTimeWorkRequestBuilder<UploadProductWork>()
                    .setInputData(data)
                    .build()
                workManager.enqueue(request)

                workManager.getWorkInfoByIdLiveData(request.id).observe(this, Observer {
                    when(it.state){
                        
                    }
                })
            }
        }
    }

}