package com.example.compose_ta09

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.compose_ta09.R
import com.example.compose_ta09.services.RetrofitClient
import com.example.compose_ta09.models.PlantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun HomeScreen(navController: NavHostController, jwtToken: String?) {
    var searchQuery by remember { mutableStateOf("") }
    var filteredPlants by remember { mutableStateOf<List<PlantResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Fungsi untuk melakukan pencarian tanaman
    fun searchPlants(query: String) {
        isLoading = true
        RetrofitClient.apiService.searchPlants(query, null, null, null).enqueue(object : Callback<List<PlantResponse>> {
            override fun onResponse(call: Call<List<PlantResponse>>, response: Response<List<PlantResponse>>) {
                isLoading = false
                if (response.isSuccessful) {
                    filteredPlants = response.body() ?: emptyList()
                } else {
                    Toast.makeText(navController.context, "Gagal memuat tanaman", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PlantResponse>>, t: Throwable) {
                isLoading = false
                Toast.makeText(navController.context, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF4E9))
            .padding(16.dp)
    ) {
        // ====================
        // HEADER DENGAN LOGO
        // ====================
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.plant),
                contentDescription = "Logo Herbal",
                modifier = Modifier.size(70.dp)
            )

            // Header
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "Jelajahi Informasi",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_green),
                )
                Text(
                    text = "Tanaman Herbal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_green),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.isNotEmpty()) {
                    searchPlants(it)
                } else {
                    filteredPlants = emptyList() // Clear search results if query is empty
                }
            },
            placeholder = { Text("Cari Nama Tanaman...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions.Default
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Jika sedang loading, tampilkan indikator loading
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // List tanaman herbal berdasarkan hasil pencarian
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPlants) { plant ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .shadow(6.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Park,
                                contentDescription = "Herbal Icon",
                                tint = Color(0xFF498553),
                                modifier = Modifier.size(50.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = plant.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF498553),
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Row {
                                    repeat(5) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Star",
                                            tint = Color(0xFFFFD700),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            TextButton(onClick = { navController.navigate("detail/${plant.plantId}") }) {
                                Text("Lihat Detail", color = Color(0xFF498553))
                            }
                        }
                    }
                }
            }
        }
    }
}
