package com.example.compose_ta09

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val plantName = intent.getStringExtra("plantName") ?: "Tanaman"

        setContent {
            DetailScreen(plantName, onBack = { finish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(plantName: String, onBack: () -> Unit) {
    var isLiked by remember { mutableStateOf(false) }
    var userRating by remember { mutableStateOf(4) }
    var comment by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf(listOf("Pengguna 2 Membalas Informasi Sebelumnya")) }
    val context = LocalContext.current
    val backgroundColor = colorResource(id = R.color.soft_green)
    val currentTime = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()).format(Date())
    val postDateTime = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()).format(Date())

    var userRatings by remember { mutableStateOf(mutableListOf(userRating)) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Postingan", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isLiked = !isLiked }) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Lihat informasi tentang tanaman ini: $plantName")
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
                onSendComment = {
                    if (comment.isNotBlank()) {
                        comments = comments + comment
                        comment = ""
                    }
                }
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Nama Pengguna 1", fontWeight = FontWeight.Bold)
                    Text(postDateTime, fontSize = 12.sp, color = Color.Gray) // Menampilkan tanggal dan waktu postingan
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("Nama Tanaman:", fontWeight = FontWeight.Bold)
            Text(plantName)

            Spacer(modifier = Modifier.height(4.dp))
            Text("Kandungan Tanaman:", fontWeight = FontWeight.Bold)
            Text("Kandungan Tanaman Kandungan Tanaman")

            Spacer(modifier = Modifier.height(4.dp))
            Text("Manfaat Tanaman:", fontWeight = FontWeight.Bold)
            Text("Manfaat Tanaman, Manfaat Tanaman")

            Spacer(modifier = Modifier.height(4.dp))
            Text("Cara Pengolahan:", fontWeight = FontWeight.Bold)
            Text("1. Cara pengolahan 1\n2. Cara pengolahan 2")

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
            val totalRating = userRatings.sum()
            Text("Total Rating: $totalRating/${userRatings.size * 5}", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))
            Text("Beri Rating:", fontWeight = FontWeight.Bold)
            Row {
                for (i in 1..5) {
                    IconButton(onClick = {
                        userRating = i
                        userRatings.add(i)
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
            Text("Review Pengguna Lainnya:", fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Nama Pengguna 2", fontWeight = FontWeight.Bold)
                    Text(comments.first())
                    Text(currentTime, fontSize = 12.sp, color = Color.Gray)
                }
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
        color = Color.Transparent,
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
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSendComment() }),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
            )
            IconButton(onClick = onSendComment) {
                Icon(Icons.Filled.Send, contentDescription = "Send Comment")
            }
        }
    }
}
