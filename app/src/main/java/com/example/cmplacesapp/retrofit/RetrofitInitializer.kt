package com.example.cmplacesapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    fun init() {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.23:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}