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
import kotlinx.coroutines.delay

class MetaMaskWebViewClient : WebViewClient() {
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
                Log.e("MetaMask", "Failed to open MetaMask", e)
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

fun isMetaMaskInstalled(context: Context): Boolean {
    val pm = context.packageManager
    return try {
        pm.getPackageInfo("io.metamask", 0)
        true
    } catch (e: Exception) {
        false
    }
}

@Composable
fun ConnectMetaScreen(navController: NavController) {
    var isWalletConnected by remember { mutableStateOf(false) }
    var walletAddress by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }

    // Timeout handling
    LaunchedEffect(loading) {
        if (loading) {
            delay(10000) // 10 detik timeout
            if (loading) {
                loading = false
                try {
                    val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("metamask://browser")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("MetaMask", "Fallback failed", e)
                    // Jika MetaMask tidak terpasang, arahkan ke halaman unduhan
                    val installIntent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://metamask.io/download.html")).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(installIntent)
                }
            }
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webView = this
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                }
                webViewClient = MetaMaskWebViewClient()

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun sendWalletAddress(address: String) {
                        walletAddress = address
                        isWalletConnected = true
                        loading = false
                    }

                    @JavascriptInterface
                    fun connectionFailed() {
                        loading = false
                    }
                }, "Android")

                loadUrl("file:///android_asset/web3page.html")
            }
        },
        modifier = Modifier.fillMaxSize()
    )

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
                            webView?.post {
                                webView?.evaluateJavascript(""" 
                                    if (typeof connectMetaMask === 'function') {
                                        connectMetaMask();
                                    } else {
                                        console.error('connectMetaMask not found');
                                        window.Android.connectionFailed();
                                    }
                                """.trimIndent(), null)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    if (loading) {
                        Box(modifier = Modifier.size(20.dp)) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
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
                        "As a Guest",
                        fontSize = 14.sp,
                        color = Color(0xFF2E7D32),
                        textDecoration = TextDecoration.Underline
                    )
                }

                if (isWalletConnected && walletAddress != null) {
                    LaunchedEffect(walletAddress) {
                        navController.navigate("register/${walletAddress}")
                    }
                }
            }
        }
    }
}
