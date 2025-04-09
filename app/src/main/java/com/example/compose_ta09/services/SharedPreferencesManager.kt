package com.example.compose_ta09.services

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {

    private const val PREF_NAME = "user_pref"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_WALLET_ADDRESS = "wallet_address"

    // Menggunakan SharedPreferences dengan mode PRIVATE
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Mengecek apakah pengguna sudah login
    fun isUserLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Menyimpan status login pengguna dan wallet address
    fun saveUserLoginState(context: Context, isLoggedIn: Boolean, walletAddress: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        walletAddress?.let {
            editor.putString(KEY_WALLET_ADDRESS, it)
        }
        editor.apply() // apply() digunakan karena kita tidak butuh hasil langsung, asinkron
    }

    // Mendapatkan alamat wallet pengguna
    fun getUserWalletAddress(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_WALLET_ADDRESS, null)
    }

    // Menghapus data pengguna (alamat wallet dan status login)
    fun clearUserData(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_WALLET_ADDRESS)
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.apply() // apply() digunakan untuk perubahan asinkron
    }

    // Melakukan logout (menghapus semua data pengguna)
    fun logoutUser(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear() // Menghapus semua data di SharedPreferences
        editor.apply() // apply() digunakan untuk perubahan asinkron
    }
}
