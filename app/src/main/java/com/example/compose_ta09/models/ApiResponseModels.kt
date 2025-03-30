package com.example.compose_ta09.models

// Data class untuk response login
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)

// Data class untuk request registrasi
data class RegisterRequest(
    val fullName: String,
    val password: String,
    val role: String,
    val walletAddress: String // Menambahkan wallet address untuk registrasi
)

// Data class untuk response registrasi
data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null // Optional, jika token diberikan saat registrasi
)

// Data class untuk response profil pengguna
data class UserDataResponse(
    val fullName: String, // Nama lengkap pengguna
    val walletAddress: String, // Alamat wallet pengguna
    val role: String, // Role pengguna (Pengguna/Validator)
    val isLoggedIn: Boolean // Status login pengguna
)
