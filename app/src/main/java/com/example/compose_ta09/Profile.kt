package com.example.compose_ta09

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose_ta09.services.RetrofitClient
import com.example.compose_ta09.models.UserDataResponse
import com.example.compose_ta09.ui.viewModels.EventSinkMetaMask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ProfileScreen(navController: NavController, jwtToken: String?, eventSink: (EventSinkMetaMask) -> Unit) {
    val context = LocalContext.current
    var userData by remember { mutableStateOf<UserDataResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fungsi untuk mendapatkan data pengguna
    fun getUserData() {
        if (jwtToken == null) {
            // Jika token tidak ada, arahkan kembali ke halaman login
            navController.navigate("login")
        } else {
            RetrofitClient.apiService.getUserData("Bearer $jwtToken").enqueue(object : Callback<UserDataResponse> {
                override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                    if (response.isSuccessful) {
                        userData = response.body()
                        isLoading = false
                    } else {
                        Toast.makeText(context, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                    Toast.makeText(context, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            })
        }
    }

    // Fungsi untuk logout dan men-disconnect wallet MetaMask
    fun logout() {
        if (jwtToken != null) {
            RetrofitClient.apiService.logout("Bearer $jwtToken").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Menampilkan pesan logout sukses
                        Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()

                        // Trigger Disconnect event MetaMask
                        eventSink(EventSinkMetaMask.Disconnect)

                        // Setelah logout, coba untuk disconnect MetaMask Wallet
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("metamask://disconnect")).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Jika MetaMask tidak terpasang atau tidak bisa disconnect, arahkan ke halaman unduh MetaMask
                            val installIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metamask.io/download.html")).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(installIntent)
                        }

                        // Setelah disconnect, arahkan pengguna ke halaman awal (connectMeta)
                        navController.navigate("connectMeta") {
                            popUpTo("main") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Gagal logout", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Gagal logout", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Ambil data pengguna ketika pertama kali membuka halaman
    LaunchedEffect(Unit) {
        getUserData()
    }

    if (isLoading) {
        // Menampilkan indikator loading saat mengambil data
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Menampilkan data pengguna jika berhasil diambil
        userData?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nama: ${it.fullName}", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                Text("Wallet Address: ${it.walletAddress}", fontSize = 18.sp, modifier = Modifier.padding(8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Logout untuk logout dan disconnect wallet
                Button(
                    onClick = { logout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Logout", fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}
