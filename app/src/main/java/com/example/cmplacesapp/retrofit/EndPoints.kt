package com.example.cmplacesapp.retrofit

import retrofit2.Call
import retrofit2.http.*


interface EndPoints {

    @FormUrlEncoded
    @POST("user/login")
    fun Login(@Field("username") username: String?,@Field("password") password: String?): Call<User>
}