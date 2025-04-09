package com.example.compose_ta09.services

import android.os.Build
import android.util.Log
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Cek apakah perangkat adalah Emulator atau fisik
    private fun getBaseUrl(): String {
        return if (isRunningOnEmulator()) {
            "http://10.0.2.2:5000/" // Untuk Emulator
        } else {
            // Gunakan IP lokal dinamis jika berjalan di perangkat fisik
            "http://${getLocalIpAddress()}:5000/"
        }
    }

    // Cek apakah aplikasi berjalan di emulator
    private fun isRunningOnEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
    }

    // Mendapatkan IP lokal perangkat
    private fun getLocalIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    // Cek apakah alamat IP bukan localhost dan bukan alamat IPv6
                    if (!address.isLoopbackAddress && address is InetAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RetrofitClient", "Error getting IP address: ${e.message}")
        }
        return "192.168.50.94" // Jika gagal, gunakan IP default
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
