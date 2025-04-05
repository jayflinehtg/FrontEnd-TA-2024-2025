package com.example.compose_ta09.services

import com.example.compose_ta09.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Endpoint untuk login
    @POST("api/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>  // <-- Perbaikan di sini, menggunakan LoginRequest

    // Endpoint untuk register
    @POST("api/auth/register")
    fun register(@Body userData: RegisterRequest): Call<RegisterResponse>

    // Endpoint untuk mendapatkan data pengguna
    @GET("api/auth/userData")
    fun getUserData(@Header("Authorization") token: String): Call<UserDataResponse>

    // Endpoint untuk logout
    @POST("api/auth/logout")
    fun logout(@Header("Authorization") token: String): Call<Void>

    // Endpoint untuk mengecek apakah user sudah login
    @GET("api/auth/isUserLoggedIn")
    fun isUserLoggedIn(@Header("Authorization") token: String): Call<Boolean>

    // Endpoint untuk menambahkan data tanaman
    @POST("api/plant/add")
    fun addPlant(@Header("Authorization") token: String, @Body plantData: PlantRequest): Call<PlantResponse>

    // Endpoint untuk mengambil data tanaman berdasarkan plantId
    @GET("api/plant/{plantId}")
    fun getPlant(@Path("plantId") plantId: String): Call<PlantResponse>

    // Endpoint untuk memberi rating pada tanaman
    @POST("api/plant/rate")
    fun ratePlant(@Header("Authorization") token: String, @Body rateData: RateRequest): Call<Void>

    @GET("api/plant/ratings/{plantId}")
    fun getPlantRatings(@Path("plantId") plantId: String): Call<List<Int>>

    // Fungsi untuk mendapatkan rata-rata rating
    @GET("api/plant/averageRating/{plantId}")
    fun getAverageRating(@Path("plantId") plantId: String): Call<Map<String, String>>

    // Endpoint untuk memberi like pada tanaman
    @POST("api/plants/like")
    fun likePlant(@Body likeRequest: LikeRequest): Call<LikeResponse>

    // Endpoint untuk memberi komentar pada tanaman
    @POST("api/plant/comment")
    fun commentPlant(@Header("Authorization") token: String, @Body commentData: CommentRequest): Call<Void>

    // Endpoint untuk mencari tanaman berdasarkan nama, komposisi, dll
    @GET("api/plant/search")
    fun searchPlants(
        @Query("name") name: String?,
        @Query("namaLatin") namaLatin: String?,
        @Query("komposisi") komposisi: String?,
        @Query("kegunaan") kegunaan: String?
    ): Call<List<PlantResponse>>

    // Endpoint untuk mendapatkan komentar dari tanaman berdasarkan plantId
    @GET("api/plant/comments/{plantId}")
    fun getComments(@Path("plantId") plantId: String): Call<List<CommentResponse>>
}
