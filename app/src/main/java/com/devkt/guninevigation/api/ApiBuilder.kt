package com.devkt.guninevigation.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiBuilder {
    val api: ApiService = Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl("https://guni-nevigation-api.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}