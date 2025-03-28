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
    val role: String
)

// Data class untuk response registrasi
data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null // Optional, jika token diberikan saat registrasi
)
