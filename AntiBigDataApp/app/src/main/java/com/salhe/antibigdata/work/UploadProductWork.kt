package com.salhe.antibigdata.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.salhe.antibigdata.Key
import com.salhe.antibigdata.dao.ProductsDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent

class UploadProductWork(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface UploadWorkEntryPoint {
        fun productsDao(): ProductsDao
    }

    override fun doWork(): Result {
        val id = inputData.getLong(Key.ID, -1)
        // ID无效
        if (id == -1L) {
            return Result.failure()
        }

        // 拿到dao
        val uploadWorkEntryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            UploadWorkEntryPoint::class.java
        )
        val productsDao = uploadWorkEntryPoint.productsDao()

        // 获得产品信息
        val product = productsDao.loadProductById(id)

        // TODO: 上传数据

        return Result.success()
    }
}