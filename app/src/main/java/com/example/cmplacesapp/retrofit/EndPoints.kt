package com.example.cmplacesapp.retrofit

import retrofit2.Call
import retrofit2.http.*
import java.text.SimpleDateFormat


interface EndPoints {

        @FormUrlEncoded
        @POST("user/login")
        fun Login(@Field("username") username: String?,@Field("password") password: String?): Call<User>

    @FormUrlEncoded
    @POST("event/insert")
    fun InsertIncidente(@Field("title") title: String?, @Field("description") description: String?,
                        @Field("latitude") latitude: String?, @Field("longitude") longitude: String?, @Field("image") image: String?,
                        @Field("user_id") user_id: Int?,@Field("tipoIncidente") tipoIncidente: String?
    ): Call<Incidentes>
    @FormUrlEncoded
    @POST("event/get")
    fun getIncidents(@Field("lixo") lixo: Boolean?, @Field("acidente") acidente: Boolean?,
                     @Field("obras") obras: Boolean?, @Field("outros") outros: Boolean?): Call<List<Incidentes>>

    @FormUrlEncoded
    @POST("event/update")
    fun UpdateIncidente(@Field("id") id: Int?,@Field("title") title: String?,@Field("description") description: String?): Call<Int>

    @FormUrlEncoded
    @POST("event/delete")
    fun deleteIncidente(@Field("id") id: Int?): Call<Int>
}