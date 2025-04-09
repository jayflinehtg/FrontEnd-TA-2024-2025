package com.example.compose_ta09.models

// Data class untuk request login
data class LoginRequest(
    val walletAddress: String, // Alamat wallet pengguna
    val password: String // Kata sandi pengguna
)

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
    val isLoggedIn: Boolean // Status login pengguna
)

// Model untuk request menambahkan tanaman
data class PlantRequest(
    val name: String,
    val namaLatin: String,
    val komposisi: String,
    val kegunaan: String,
    val caraPengolahan: String,
    val ipfsHash: String // IPFS hash untuk gambar tanaman
)

// Model untuk response data tanaman
data class PlantResponse(
    val name: String,
    val namaLatin: String,
    val komposisi: String,
    val kegunaan: String,
    val caraPengolahan: String,
    val ipfsHash: String,
    val ratingTotal: String,
    val ratingCount: String,
    val likeCount: String,
    val owner: String,
    val plantId: String
)

// Model untuk request memberi like pada tanaman
data class LikeRequest(
    val plantId: String // ID tanaman yang akan diberi like
)

// Model untuk response saat memberi like pada tanaman
data class LikeResponse(
    val success: Boolean, // Status sukses atau gagal
    val message: String,  // Pesan yang terkait dengan status operasinya
    val txHash: String? = null,  // Opsional, bisa menyertakan txHash dari transaksi smart contract
    val plantId: String // ID tanaman yang diberi like
)

// Model untuk request rating tanaman
data class RateRequest(
    val plantId: String,
    val rating: Int
)

// Model untuk response saat memberikan rating pada tanaman
data class RateResponse(
    val success: Boolean, // Status sukses atau gagal
    val message: String,  // Pesan yang terkait dengan status ratingnya
    val txHash: String? = null, // Opsional, bisa menyertakan txHash dari transaksi smart contract
    val plantId: String, // ID tanaman yang diberi rating
    val rating: Int // Rating yang diberikan
)

// Model untuk request komentar tanaman
data class CommentRequest(
    val plantId: String,
    val comment: String
)

// Model untuk response komentar tanaman
data class CommentResponse(
    val user: String, // Alamat pengguna
    val comment: String, // Isi komentar
    val timestamp: String // Waktu komentar
)

// Model untuk request logout
data class LogoutRequest(
    val token: String // Token yang digunakan untuk logout
)

// Model untuk response logout
data class LogoutResponse(
    val success: Boolean, // Status sukses atau gagal
    val message: String  // Pesan terkait status logout
)

// Model untuk request mencari tanaman
data class SearchPlantRequest(
    val name: String? = null,
    val namaLatin: String? = null,
    val komposisi: String? = null,
    val kegunaan: String? = null
)

// Model untuk response mencari tanaman
data class SearchPlantResponse(
    val success: Boolean,
    val plants: List<PlantResponse> // Daftar tanaman yang ditemukan
)

// Model untuk request memeriksa status login pengguna
data class IsUserLoggedInRequest(
    val token: String // Token untuk memeriksa status login
)

// Model untuk response memeriksa status login pengguna
data class IsUserLoggedInResponse(
    val success: Boolean,
    val isLoggedIn: Boolean // Status login pengguna
)
