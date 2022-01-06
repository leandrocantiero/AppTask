package com.example.tasks.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {
        private lateinit var mInstance: Retrofit
        private const val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            if (!Companion::mInstance.isInitialized) {
                mInstance = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return mInstance
        }

        fun <S> createService(service: Class<S>): S {
            return getRetrofitInstance().create(service)
        }
    }
}