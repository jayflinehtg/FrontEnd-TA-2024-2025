package com.example.compose_ta09

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)), // Warna background hijau muda
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF98CDA0)), // Warna hijau dengan sudut melengkung
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.85f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LOGIN",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32) // Warna hijau gelap
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Input Kata Sandi
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null // Hapus error saat mengetik
                    },
                    label = { Text("Kata Sandi") },
                    placeholder = { Text("Masukkan Kata Sandi") },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = passwordError != null,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        errorContainerColor = Color.White
                    )
                )

                // Tampilkan pesan error jika ada
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Tombol Masuk
                Button(
                    onClick = {
                        if (password.isEmpty()) {
                            passwordError = "Kata sandi tidak boleh kosong"
                        } else {
                            navController.navigate("main") // Pindah ke halaman utama
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)), // Warna hijau gelap
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Masuk", fontSize = 14.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Link ke Halaman Register
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(
                        "Belum memiliki akun? Daftar disini",
                        fontSize = 14.sp,
                        color = Color(0xFF4E4E4E)
                    )
                }
            }
        }
    }
}
