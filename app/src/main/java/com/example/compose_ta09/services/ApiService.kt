package com.example.compose_ta09.services

import com.example.compose_ta09.models.LoginResponse
import com.example.compose_ta09.models.RegisterRequest
import com.example.compose_ta09.models.RegisterResponse
import com.example.compose_ta09.models.UserDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // Endpoint untuk login
    @POST("api/auth/login")
    fun login(@Body password: String): Call<LoginResponse>

    // Endpoint untuk register
    @POST("api/auth/register")
    fun register(@Body userData: RegisterRequest): Call<RegisterResponse>

    // Endpoint untuk mendapatkan data pengguna
    @GET("api/auth/userData")
    fun getUserData(@Header("Authorization") token: String): Call<UserDataResponse>

    // Endpoint untuk logout
    @POST("api/auth/logout")
    fun logout(@Header("Authorization") token: String): Call<Void>
}
