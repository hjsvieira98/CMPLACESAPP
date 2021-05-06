package com.example.cmplacesapp.retrofit

import java.util.*

data class Incidentes(
    val id: Int,
    val title: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val image: String,
    val user_id: Int,
    val tipoIncidente:String
    )