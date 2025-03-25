package com.example.compose_ta09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose_ta09.ui.theme.COMPOSE_TA09Theme

@Composable
fun RegisterScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var publicKey by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.soft_green))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "REGISTER",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.dark_green),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.green)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Input Nama Lengkap
                TextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nama Lengkap", color = Color.Black) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && fullName.isEmpty()
                )
                if (showError && fullName.isEmpty()) {
                    Text(
                        text = "Nama lengkap harus diisi!",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input Email
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.Black) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && email.isEmpty()
                )
                if (showError && email.isEmpty()) {
                    Text(
                        text = "Email harus diisi!",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input Public Key
                TextField(
                    value = publicKey,
                    onValueChange = { publicKey = it },
                    label = { Text("Public Key", color = Color.Black) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && publicKey.isEmpty()
                )
                if (showError && publicKey.isEmpty()) {
                    Text(
                        text = "Public Key harus diisi!",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input Kata Sandi
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Kata Sandi", color = Color.Black) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && password.isEmpty()
                )
                if (showError && password.isEmpty()) {
                    Text(
                        text = "Kata sandi harus diisi!",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Daftar
                Button(
                    onClick = {
                        if (fullName.isNotEmpty() && email.isNotEmpty() && publicKey.isNotEmpty() && password.isNotEmpty()) {
                            // Handle register logic

                            // Navigasi ke login setelah berhasil daftar
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            showError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_green)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "Daftar", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Teks Navigasi ke Login
        TextButton(onClick = { navController.navigate("login") }) {
            Text(
                text = "Sudah memiliki Akun? Masuk sekarang!",
                color = colorResource(id = R.color.purple_500),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    COMPOSE_TA09Theme {
        RegisterScreen(rememberNavController())
    }
}
