package com.example.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {
    private val mRemote = RetrofitClient.createService(PriorityService::class.java)
    private val mPriorityDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    fun get() {
        if (!isConnectionAvailable(context)) {
            return
        }

        val call: Call<List<PriorityModel>> = mRemote.get()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                res: Response<List<PriorityModel>>
            ) {
                if (res.code() == TaskConstants.HTTP.SUCCESS) {
                    res.body()?.let { mPriorityDatabase.save(it) }
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {}

        })
    }

    fun list() = mPriorityDatabase.list()

    fun getById(id: Int) = mPriorityDatabase.getById(id)
}