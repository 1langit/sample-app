package com.example.suitmediatest.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://reqres.in/api/"
    private val okHttpClient = buildOkHttpClient()

    fun getInstance(): ApiService {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return builder.create(ApiService::class.java)
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}