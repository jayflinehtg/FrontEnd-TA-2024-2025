package com.example.compose_ta09

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
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
            text = "LOGIN",
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
                TextButton(onClick = { /* TODO: Implement e-wallet connection logic */ }) {
                    Text(
                        text = "Hubungkan ke E-Wallet",
                        color = colorResource(id = R.color.dark_green),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.Black) },
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
                    isError = showError && password.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (showError && password.isEmpty()) {
                    Text(
                        text = "Password harus diisi!",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (password.isNotEmpty()) {
                            // Mengarahkan ke RegisterScreen saat tombol login ditekan
                            navController.navigate("register")
                        } else {
                            showError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_green)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "Login", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val annotatedString = buildAnnotatedString {
            append("Belum memiliki Akun? ")
            pushStringAnnotation(tag = "register", annotation = "register")
            withStyle(style = SpanStyle(color = colorResource(id = R.color.purple_500), fontWeight = FontWeight.Bold)) {
                append("Daftar Disini!")
            }
            pop()
        }

        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "register", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate("register")
                    }
            }
        )
    }
}
