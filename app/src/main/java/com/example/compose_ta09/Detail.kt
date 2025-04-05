package com.example.compose_ta09

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_ta09.models.CommentRequest
import com.example.compose_ta09.models.CommentResponse
import com.example.compose_ta09.models.LikeRequest
import com.example.compose_ta09.models.LikeResponse
import com.example.compose_ta09.services.RetrofitClient
import com.example.compose_ta09.models.PlantResponse
import com.example.compose_ta09.models.RateRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(plantId: String, onBack: () -> Unit) {
    var plantDetails by remember { mutableStateOf<PlantResponse?>(null) }
    var isLiked by remember { mutableStateOf(false) }
    var userRating by remember { mutableStateOf(4) }
    var comment by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf(listOf<CommentResponse>()) }
    var ratingsList by remember { mutableStateOf(listOf<Int>()) } // List untuk menyimpan semua rating
    var averageRating by remember { mutableStateOf(0.0) } // Untuk menyimpan rata-rata rating

    val context = LocalContext.current
    val backgroundColor = colorResource(id = R.color.soft_green)
    val currentTime = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()).format(Date())

    // Memuat data tanaman berdasarkan plantId
    LaunchedEffect(plantId) {
        RetrofitClient.apiService.getPlant(plantId).enqueue(object : Callback<PlantResponse> {
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                if (response.isSuccessful) {
                    plantDetails = response.body()

                    // Memuat komentar setelah data tanaman berhasil diambil
                    RetrofitClient.apiService.getComments(plantId).enqueue(object : Callback<List<CommentResponse>> {
                        override fun onResponse(call: Call<List<CommentResponse>>, response: Response<List<CommentResponse>>) {
                            if (response.isSuccessful) {
                                comments = response.body() ?: listOf()
                            } else {
                                Toast.makeText(context, "Gagal memuat komentar", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
                            Toast.makeText(context, "Gagal menghubungi server untuk komentar", Toast.LENGTH_SHORT).show()
                        }
                    })

                    // Memuat semua rating untuk menghitung rata-rata
                    RetrofitClient.apiService.getPlantRatings(plantId).enqueue(object : Callback<List<Int>> {
                        override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                            if (response.isSuccessful) {
                                ratingsList = response.body() ?: listOf()
                                averageRating = ratingsList.average()
                            } else {
                                Toast.makeText(context, "Gagal memuat ratings", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                            Toast.makeText(context, "Gagal menghubungi server untuk ratings", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(context, "Gagal memuat data tanaman", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                Toast.makeText(context, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk memberikan like pada tanaman
    fun toggleLike() {
        plantDetails?.let {
            RetrofitClient.apiService.likePlant(LikeRequest(it.plantId)).enqueue(object : Callback<LikeResponse> {
                override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                    if (response.isSuccessful) {
                        isLiked = !isLiked // Toggle like status
                    } else {
                        Toast.makeText(context, "Gagal memberi like", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                    Toast.makeText(context, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Fungsi untuk memberi rating pada tanaman
    fun ratePlant() {
        plantDetails?.let {
            RetrofitClient.apiService.ratePlant("Bearer YOUR_JWT_TOKEN", RateRequest(it.plantId, userRating)).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Rating berhasil", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Gagal memberi rating", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Fungsi untuk menambah komentar
    fun addComment() {
        plantDetails?.let {
            RetrofitClient.apiService.commentPlant("Bearer YOUR_JWT_TOKEN", CommentRequest(it.plantId, comment)).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Setelah berhasil, tambahkan komentar ke list dan reset input
                        comments = comments + CommentResponse(it.plantId, comment, currentTime)
                        comment = "" // Reset input komentar
                    } else {
                        Toast.makeText(context, "Gagal menambahkan komentar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Postingan", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { toggleLike() }) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Lihat informasi tentang tanaman ini: ${plantDetails?.name}")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"))
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                }
            )
        },
        bottomBar = {
            CommentInputSection(
                comment = comment,
                onCommentChange = { comment = it },
                onSendComment = { addComment() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            if (plantDetails != null) {
                // Menampilkan data tanaman yang sudah dimuat dari API
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.baseline_person_24),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Nama Pengguna 1", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text(currentTime, fontSize = 12.sp, color = Color.Gray) // Menampilkan tanggal dan waktu postingan
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
                Text("Nama Tanaman:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(plantDetails?.name ?: "Tanaman", fontSize = 15.sp)

                Spacer(modifier = Modifier.height(4.dp))
                Text("Kandungan Tanaman:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(plantDetails?.komposisi ?: "Kandungan Tanaman", fontSize = 15.sp)

                Spacer(modifier = Modifier.height(4.dp))
                Text("Manfaat Tanaman:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(plantDetails?.kegunaan ?: "Manfaat Tanaman", fontSize = 15.sp)

                Spacer(modifier = Modifier.height(4.dp))
                Text("Cara Pengolahan:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(plantDetails?.caraPengolahan ?: "Cara pengolahan tanaman", fontSize = 15.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(R.drawable.jahe),
                        contentDescription = "Plant Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Average Rating: %.2f".format(averageRating), fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))
                Text("Beri Rating:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Row {
                    for (i in 1..5) {
                        IconButton(onClick = {
                            userRating = i
                            ratePlant() // Submit rating ke API
                        }) {
                            Icon(
                                painterResource(if (i <= userRating) R.drawable.baseline_star_24 else R.drawable.baseline_star_border_24),
                                contentDescription = "Star Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Review Pengguna Lainnya:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Nama Pengguna 2", fontWeight = FontWeight.Bold)
                        Text(comments.first().comment, fontSize = 15.sp)
                        Text(currentTime, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            } else {
                // Tampilkan loading atau pesan jika data tanaman belum dimuat
                Text("Memuat data tanaman...", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CommentInputSection(
    comment: String,
    onCommentChange: (String) -> Unit,
    onSendComment: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(id = R.color.soft_green),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = comment,
                onValueChange = onCommentChange,
                placeholder = { Text("Tambahkan Komentar Anda...", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.soft_green),
                    unfocusedContainerColor = colorResource(id = R.color.soft_green),
                    disabledContainerColor = colorResource(id = R.color.soft_green)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSendComment() }),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
            )
            IconButton(onClick = onSendComment) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Comment")
            }
        }
    }
}
