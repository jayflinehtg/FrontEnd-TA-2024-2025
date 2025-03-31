package com.example.compose_ta09

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ConnectMetaScreen(navController: NavController) {
    var isWalletConnected by remember { mutableStateOf(false) }
    var walletAddress by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // WebView untuk menampilkan halaman MetaMask
    val webView = remember { mutableStateOf<WebView?>(null) }

    // Setting WebView dan memuat halaman HTML yang berisi ethers.js
    LaunchedEffect(Unit) {
        val myWebView = WebView(context)
        myWebView.settings.javaScriptEnabled = true
        myWebView.addJavascriptInterface(object {
            @JavascriptInterface
            fun sendWalletAddress(address: String) {
                walletAddress = address
                isWalletConnected = true
                Log.d("MetaMask", "Wallet Address: $address")
            }
        }, "Android")

        myWebView.loadUrl("file:///android_asset/web3page.html")
        webView.value = myWebView
    }

    // Tampilan Connect Wallet
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.soft_green)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.85f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Akses Tanaman Herbal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_green)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Hubungkan dompet Anda untuk berkontribusi & menjelajahi dunia tanaman herbal!",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Ketika tombol Connect Wallet diklik
                        webView.value?.loadUrl("javascript:connectMetaMask()")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Connect Wallet", fontSize = 14.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("or", fontSize = 14.sp, color = Color.Gray)

                // Navigasi jika memilih "As a Guest"
                TextButton(onClick = {
                    navController.navigate("main") {
                        popUpTo("connectMeta") { inclusive = true }
                        launchSingleTop = true // Mencegah layar duplikasi
                    }
                }) {
                    Text(
                        "As a Guest",
                        fontSize = 14.sp,
                        color = Color(0xFF2E7D32),
                        textDecoration = TextDecoration.Underline
                    )
                }

                // Kondisi jika wallet terhubung
                if (isWalletConnected && walletAddress != null) {
                    // Setelah wallet terhubung, arahkan ke halaman registrasi
                    LaunchedEffect(walletAddress) {
                        navController.navigate("register/${walletAddress}") // Kirim walletAddress ke halaman registrasi
                    }
                }
            }
        }
    }
}
