package com.example.compose_ta09.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/") // URL server backend kamu
        .addConverterFactory(GsonConverterFactory.create()) // Converter JSON ke objek
        .build()

    // Mendapatkan ApiService
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
