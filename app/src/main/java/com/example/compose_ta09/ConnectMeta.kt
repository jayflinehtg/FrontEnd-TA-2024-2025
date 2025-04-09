package com.example.compose_ta09

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.example.compose_ta09.helper.OnEvent
import com.example.compose_ta09.ui.viewModels.EventSinkMetaMask
import kotlinx.coroutines.delay

// WebView client untuk menangani URL MetaMask
class MetaMaskWebViewClient : WebViewClient() {
    @Suppress("DEPRECATION")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url == null) return false

        Log.d("MetaMask", "Intercepting URL: $url")

        val isMetaMaskUrl = url.startsWith("metamask:") ||
                url.contains("metamask.app.link") ||
                url.contains("chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn")

        return if (isMetaMaskUrl) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                view?.context?.startActivity(intent)
                true
            } catch (e: Exception) {
                Log.e("MetaMask", "Gagal membuka MetaMask", e)
                // Jika MetaMask tidak terpasang, arahkan ke halaman unduhan
                val installIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://metamask.io/download.html")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                view?.context?.startActivity(installIntent)
                true
            }
        } else {
            false
        }
    }
}

@Composable
fun ConnectMetaScreen(navController: NavController, eventSink: (EventSinkMetaMask) -> Unit) {
    var isWalletConnected by remember { mutableStateOf(false) }
    var walletAddress by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Timeout handling jika koneksi gagal
    LaunchedEffect(loading) {
        if (loading) {
            delay(10000) // Timeout 10 detik
            if (loading) {
                loading = false
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("metamask://browser")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("MetaMask", "Fallback gagal", e)
                    val installIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://metamask.io/download.html")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(installIntent)
                }
            }
        }
    }

    // WebView untuk meminta akses MetaMask
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }
                webViewClient = MetaMaskWebViewClient()

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun sendWalletAddress(address: String) {
                        walletAddress = address
                        isWalletConnected = true
                        loading = false
                        eventSink(EventSinkMetaMask.SendWalletAddress(address)) // Menggunakan event sink untuk mengirim wallet address
                    }

                    @JavascriptInterface
                    fun connectionFailed() {
                        loading = false
                        eventSink(EventSinkMetaMask.ConnectionFailed) // Menggunakan event sink untuk menangani koneksi gagal
                    }
                }, "Android")

                // Menjalankan JavaScript untuk meminta akses MetaMask
                evaluateJavascript("""
                    if (typeof window.ethereum !== 'undefined') {
                        ethereum.request({ method: 'eth_requestAccounts' })
                            .then(accounts => {
                                let walletAddress = accounts[0];
                                window.Android.sendWalletAddress(walletAddress); // Kirim ke Android
                            })
                            .catch(error => {
                                console.error("Error connecting to MetaMask:", error);
                                window.Android.connectionFailed();  // Jika gagal
                            });
                    } else {
                        window.Android.connectionFailed();  // Jika MetaMask tidak terpasang
                    }
                """, null)
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    // Menggunakan OnEvent untuk menangani event
    val uiEvent = remember { mutableStateOf<EventSinkMetaMask?>(null) }
    OnEvent(events = uiEvent.value ?: flowOf()) { event ->
        when (event) {
            EventSinkMetaMask.WalletConnected -> {
                isWalletConnected = true
                walletAddress?.let {
                    navController.navigate("register/${it}")
                }
            }
            EventSinkMetaMask.ConnectionFailed -> {
                Log.e("ConnectMetaScreen", "Koneksi MetaMask gagal")
                showMessage("Koneksi gagal! Pastikan MetaMask terpasang.")
            }
        }
    }

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
                        showConfirmationDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    if (loading) {
                        Box(modifier = Modifier.size(20.dp)) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                        }
                    } else {
                        Text("Connect Wallet", fontSize = 14.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("or", fontSize = 14.sp, color = Color.Gray)

                TextButton(onClick = {
                    navController.navigate("main") {
                        popUpTo("connectMeta") { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Text(
                        "Sebagai Tamu",
                        fontSize = 14.sp,
                        color = Color(0xFF2E7D32),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Konfirmasi Koneksi Wallet") },
            text = { Text("Apakah Anda yakin ingin menghubungkan dompet MetaMask?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                        if (!loading) {
                            loading = true
                            eventSink(EventSinkMetaMask.Connect) // Trigger Connect Event
                        }
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmationDialog = false }
                ) {
                    Text("Tidak")
                }
            }
        )
    }
}
