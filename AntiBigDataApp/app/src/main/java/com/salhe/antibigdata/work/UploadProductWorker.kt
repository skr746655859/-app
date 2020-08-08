package com.salhe.antibigdata.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.salhe.antibigdata.Key
import com.salhe.antibigdata.data.dao.ProductsDao
import com.salhe.antibigdata.service.ApiResult
import com.salhe.antibigdata.service.ProductRemoteService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent

class UploadProductWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface UploadWorkEntryPoint {
        fun productsDao(): ProductsDao
        fun productRemoteService(): ProductRemoteService
    }

    override fun doWork(): Result {
        val id = inputData.getLong(Key.ID, -1)
        // ID无效
        if (id == -1L) {
            return Result.failure(workDataOf(Key.MSG to "ID无效"))
        }

        // 拿到相关服务
        val uploadWorkEntryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            UploadWorkEntryPoint::class.java
        )
        val productsDao = uploadWorkEntryPoint.productsDao()
        val productRemoteService = uploadWorkEntryPoint.productRemoteService()

        // 获得产品信息
        val product = productsDao.loadProductById(id)

        val response = productRemoteService.addProduct(product)
            .execute()
        if (response.isSuccessful) {
            val result = response.body()!!
            if (result.code == ApiResult.SUCCESS)
                return Result.success(workDataOf(Key.MSG to result.msg))
            return Result.failure(workDataOf(Key.MSG to result.msg))
        } else {
            return Result.failure(workDataOf(Key.MSG to "网络请求错误"))
        }

    }
}