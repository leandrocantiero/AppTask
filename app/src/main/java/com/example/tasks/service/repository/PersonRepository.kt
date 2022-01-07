package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header

class PersonRepository(context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(PersonService::class.java)

    fun login(email: String, password: String, listener: ApiListener<HeaderModel>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<HeaderModel> = mRemote.login(email, password)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onResponse(call: Call<HeaderModel>, res: Response<HeaderModel>) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun register(
        name: String,
        email: String,
        password: String,
        listener: ApiListener<HeaderModel>
    ) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<HeaderModel> = mRemote.register(name, email, password)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onResponse(call: Call<HeaderModel>, res: Response<HeaderModel>) {
                if (res.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(res.errorBody()!!.string(), String::class.java)

                    listener.onFailure(message)
                } else {
                    res.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }
}