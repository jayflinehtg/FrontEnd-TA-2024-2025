package com.example.compose_ta09.services

import android.content.Context
import android.content.SharedPreferences
import com.example.compose_ta09.MainActivity

object SharedPreferencesManager {

    private const val PREF_NAME = "user_pref"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_WALLET_ADDRESS = "wallet_address"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isUserLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUserLoginState(context: Context, isLoggedIn: Boolean, walletAddress: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        walletAddress?.let {
            editor.putString(KEY_WALLET_ADDRESS, it)
        }
        editor.apply()
    }

    fun getUserWalletAddress(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_WALLET_ADDRESS, null)
    }

    fun logoutUser(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}
