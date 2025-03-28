package com.example.compose_ta09

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose_ta09.models.RegisterRequest
import com.example.compose_ta09.services.RetrofitClient
import com.example.compose_ta09.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current

    var role by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var roleExpanded by remember { mutableStateOf(false) }
    val roleList = listOf("Pengguna", "Validator")

    var roleError by remember { mutableStateOf<String?>(null) }
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Fungsi untuk register
    fun register() {
        val registerRequest = RegisterRequest(
            fullName = fullName,
            password = password,
            role = role
        )

        RetrofitClient.apiService.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    // Jika registrasi berhasil
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") // Redirect ke halaman login
                    } else {
                        Toast.makeText(context, registerResponse?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Tampilkan error jika request gagal
                    Toast.makeText(context, "Registrasi gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                // Tampilkan error jika gagal menghubungi server
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.soft_green)), // Warna background utama
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            // ==== LOGO ====
            Image(
                painter = painterResource(id = R.drawable.plant),
                contentDescription = "Logo Tanaman",
                modifier = Modifier
                    .size(130.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "REGISTER",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_green),
            )

            Text(
                text = "Daftar Akun Anda!",
                fontSize = 14.sp,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    // === DROPDOWN ROLE ===
                    ExposedDropdownMenuBox(
                        expanded = roleExpanded,
                        onExpandedChange = { roleExpanded = !roleExpanded }
                    ) {
                        OutlinedTextField(
                            value = role,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pilih Role") },
                            placeholder = { Text("Pilih Role") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            isError = roleError != null,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                errorContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = roleExpanded,
                            onDismissRequest = { roleExpanded = false },
                            modifier = Modifier
                                .background(Color.White)
                        ) {
                            roleList.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        role = selectionOption
                                        roleExpanded = false
                                        roleError = null
                                    },
                                    modifier = Modifier.background(Color.White)
                                )
                            }
                        }
                    }
                    if (roleError != null) {
                        Text(
                            text = roleError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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
                        onClick = { register() },
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
                            color = colorResource(id = R.color.purple),
                        )
                    }
                }
            }
        }
    }
}
