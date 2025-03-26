package com.example.compose_ta09

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        navController.navigate("connectMeta") {
            popUpTo("main") { inclusive = true } // Pastikan kembali ke halaman awal
        }
    }

    // Tampilan kosong karena akan segera berpindah
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator() // Animasi loading sementara pindah halaman
    }
}

