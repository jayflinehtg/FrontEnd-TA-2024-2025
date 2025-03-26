package com.example.compose_ta09

import android.widget.Toast
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF5EE)), // Warna background utama
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF98CDA0)), // Warna hijau muda
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.85f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Judul
                Text(
                    "REGISTER",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_green),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Daftar Akun Anda!",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.black)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input Nama Lengkap
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = null
                    },
                    label = { Text("Nama Lengkap") },
                    placeholder = { Text("Masukkan Nama Lengkap") },
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = fullNameError != null,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        errorContainerColor = Color.White
                    )
                )
                if (fullNameError != null) {
                    Text(
                        fullNameError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input Kata Sandi
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
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
                if (passwordError != null) {
                    Text(
                        passwordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Daftar
                Button(
                    onClick = {
                        // Validasi input
                        fullNameError = if (fullName.isBlank()) "Nama Lengkap tidak boleh kosong!" else null
                        passwordError = if (password.isBlank()) "Kata Sandi tidak boleh kosong!" else null

                        if (fullNameError == null && passwordError == null) {
                            Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") // Redirect ke halaman login
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Daftar", fontSize = 14.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Link Login
                TextButton(onClick = { navController.navigate("login") }) {
                    Text(
                        "Sudah memiliki akun? Masuk sekarang!",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.purple_500),
                    )
                }
            }
        }
    }
}
