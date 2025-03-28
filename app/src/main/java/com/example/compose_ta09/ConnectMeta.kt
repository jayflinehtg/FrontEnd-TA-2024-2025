package com.example.compose_ta09

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ConnectMetaScreen(navController: NavController) {
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
                    onClick = { navController.navigate("register") }, // Pindah ke RegisterScreen
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Connect Wallet", fontSize = 14.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("or", fontSize = 14.sp, color = Color.Gray)

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
            }
        }
    }
}