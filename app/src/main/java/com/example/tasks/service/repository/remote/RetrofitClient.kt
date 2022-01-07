package com.example.tasks.service.repository.remote

import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {
        private lateinit var mInstance: Retrofit
        private const val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"
        private var tokenKey: String = ""
        private var personKey: String = ""

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val req = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .build()

                    return chain.proceed(req)
                }
            })

            if (!Companion::mInstance.isInitialized) {
                mInstance = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return mInstance
        }

        fun addHeader(tokenKey: String, personKey: String) {
            this.tokenKey = tokenKey
            this.personKey = personKey
        }

        fun <S> createService(service: Class<S>): S {
            return getRetrofitInstance().create(service)
        }
    }
}