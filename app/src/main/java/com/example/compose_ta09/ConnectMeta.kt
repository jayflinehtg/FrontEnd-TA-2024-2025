package com.example.compose_ta09

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ConnectMetaScreen(navController: NavController) {
    // State management
    var isWalletConnected by remember { mutableStateOf(false) }
    var walletAddress by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // WebView untuk menampilkan halaman MetaMask
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("MetaMask", "WebView page finished loading")
                    }
                }

                // Menambahkan JavascriptInterface untuk berinteraksi dengan Android
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun sendWalletAddress(address: String) {
                        Log.d("MetaMask", "Received wallet address: $address")
                        walletAddress = address
                        isWalletConnected = true
                        loading = false
                        Log.d("MetaMask", "Wallet Connected: $isWalletConnected, Address: $walletAddress")
                    }
                }, "Android")

                // Memuat halaman HTML dari file lokal
                loadUrl("file:///android_asset/web3page.html")
                Log.d("MetaMask", "WebView Loaded with URL: file:///android_asset/web3page.html")
            }
        },
        update = { webView ->
            Log.d("MetaMask", "WebView Updated.")
        },
        modifier = Modifier.fillMaxSize()
    )

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
                        if (!loading) {
                            loading = true
                            Log.d("MetaMask", "Connect Wallet Button Clicked")
                            // Panggil fungsi JavaScript connectMetaMask di halaman HTML
                            (context as? WebView)?.loadUrl("javascript:connectMetaMask()")
                            Log.d("MetaMask", "Calling JavaScript connectMetaMask()")
                        }
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

                // Menampilkan loading indicator saat menunggu koneksi wallet
                if (loading) {
                    CircularProgressIndicator(color = Color(0xFF2E7D32))
                    Log.d("MetaMask", "Loading: Wallet is connecting...")
                }
            }
        }
    }
}
