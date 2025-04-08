package com.example.compose_ta09.services

import android.os.Build
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress

object RetrofitClient {

    // Cek apakah perangkat adalah Emulator atau fisik
    private fun getBaseUrl(): String {
        return if (isRunningOnEmulator()) {
            "http://10.0.2.2:5000/" // Untuk Emulator
        } else {
            // Ganti dengan IP lokal komputer yang menjalankan backend
            "http://192.168.1.102:5000/"
        }
    }

    // Cek apakah aplikasi berjalan di emulator
    private fun isRunningOnEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
    }

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder().build()
        Retrofit.Builder()
            .baseUrl(getBaseUrl()) // Tentukan BASE_URL berdasarkan apakah emulator atau perangkat fisik
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
