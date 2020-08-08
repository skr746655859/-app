package com.salhe.antibigdata.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
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
class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    // DI
    @Inject
    lateinit var productsDao: ProductsDao

    @Inject
    lateinit var snowFlake: SnowFlake

    // Data
    lateinit var allProducts: LiveData<List<Product>>
    lateinit var deviceId: String

    // View
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
        initObserver()

    }

    val REQUEST_READ_PHONE_STATE = 1

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_READ_PHONE_STATE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                deviceId = getDeviceId(this)
            } else {
                Toast.makeText(this, "权限已被用户拒绝", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        var deviceId = ""
        val tm =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                REQUEST_READ_PHONE_STATE
            )
        } else {
            deviceId = if (tm.deviceId != null) {
                tm.deviceId
            } else {
                Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
            }
        }
        return deviceId
    }


    private fun initData() {
        deviceId = getDeviceId(this)

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
        allProducts.value?.forEach { product ->
            if (product.state != DataState.uploaded) {
                val data = workDataOf(Key.ID to product.id)
                val request = OneTimeWorkRequestBuilder<UploadProductWork>()
                    .setInputData(data)
                    .build()
                workManager.enqueue(request)

                workManager.getWorkInfoByIdLiveData(request.id).observe(this, Observer { workInfo ->
                    val state = when (workInfo.state) {
                        WorkInfo.State.RUNNING -> DataState.uploading
                        WorkInfo.State.FAILED -> DataState.failed
                        WorkInfo.State.SUCCEEDED -> DataState.uploaded
                        else -> DataState.wait
                    }
                    val productNew = Product(
                        product.id,
                        product.name,
                        product.price,
                        product.priceMax,
                        product.priceBeforeDiscount,
                        state
                    )
                    productsDao.insertAll(productNew)
                })
            }
        }
    }

}